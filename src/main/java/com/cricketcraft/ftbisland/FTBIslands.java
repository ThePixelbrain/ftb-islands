package com.cricketcraft.ftbisland;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cricketcraft.ftbisland.commands.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ftb.lib.LMMod;

@Mod(
    modid = Tags.MODID,
    name = Tags.MODNAME,
    version = Tags.VERSION,
    dependencies = "required-after:FTBL",
    acceptableRemoteVersions = "*")
public class FTBIslands {

    public static int maxIslands;
    public static File islandFile;
    public static Logger logger;
    public static String islandType;
    public static LMMod mod;
    private static File oldIslands;
    private static File directory;
    public static ArrayList<IslandCreator.IslandPos> islandLoc = new ArrayList<IslandCreator.IslandPos>();
    private static HashMap<String, IslandCreator.IslandPos> islands = new HashMap<>();

    @Mod.EventHandler
    public void serverLoading(FMLServerStartingEvent event) {
        logger.info("Registering commands.");
        event.registerServerCommand(new CreateIslandCommand());
        event.registerServerCommand(new DeleteIslandCommand());
        event.registerServerCommand(new JoinIslandCommand());
        event.registerServerCommand(new ListIslandCommand());
        event.registerServerCommand(new RenameIslandCommand());
        event.registerServerCommand(new SetIslandSpawnCommand());
        event.registerServerCommand(new TeleportIslandCommand());
        logger.info("Finished registering commands.");
        loadIslands();
        loadChestLoot();
        reloadIslands();
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() {
        return islands;
    }

    private void loadIslands() {
        for (int c = 0; c < maxIslands; c++) {
            addIslandToList(c);
        }
    }

    private void addIslandToList(int x) {
        if (x != 0) {
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(-x * 1000, 60, x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(-x * 1000, 60, -x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, -x * 1000));
        } else {
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, x * 1000));
        }
    }

    private void loadChestLoot() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(new File(event.getModConfigurationDirectory(), "ftbi/FTB_Islands.cfg"));
        logger = LogManager.getLogger("FTBI");
        mod = LMMod.create("FTBI");
        File dir = event.getModConfigurationDirectory();
        directory = new File(dir.getParentFile(), "local");
        oldIslands = new File(directory, "islands.ser");
        islandFile = new File(directory, "islands.json");
        if (oldIslands.exists()) {
            logger.info("Islands.ser found, attempting conversion.");
            try {
                convert();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            directory.mkdirs();
            islandFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(islandFile.getPath()));
        if (br.readLine() == null) {
            logger.info("Islands file empty, placing a default value.");
            FTBIslands.islands.put("default", new IslandCreator.IslandPos(0, 60, 0));
            try {
                saveIslands(FTBIslands.islands);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        br.close();
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        String s = new GsonBuilder().create()
            .toJson(map);
        FileUtils.writeStringToFile(islandFile, s);
    }

    public static void reloadIslands() {
        try {
            islands = FTBIslands.getIslandsFromFile();
        } catch (EOFException e) {
            // silent catch
        } catch (IOException e) {
            FTBIslands.logger.error("Couldn't get islands from save file");
            e.printStackTrace();
        }
    }

    private static HashMap<String, IslandCreator.IslandPos> getIslandsFromFile() throws IOException {
        try (FileInputStream ignored = new FileInputStream(islandFile)) {
            return new Gson().fromJson(
                FileUtils.readFileToString(islandFile),
                new TypeToken<HashMap<String, IslandCreator.IslandPos>>() {}.getType());
        }
    }

    private static class Config {

        private static Configuration config;

        public static void init(File file) {
            if (config == null) {
                config = new Configuration(file);
                loadConfig();
            }
        }

        private static void loadConfig() {
            FTBIslands.maxIslands = config.getInt(
                "Max Islands",
                "misc",
                100,
                1,
                1000,
                "The maximum amount of islands that can be created. This number will be multiplied by four."
                    + " Be careful with high numbers.");
            if (!config.hasKey("misc", "Island Type")) {
                boolean skyFactory = config
                    .getBoolean("Sky Factory", "misc", false, "Set this to true if you are playing on Sky Factory.");
                boolean platform = config.getBoolean(
                    "Platform",
                    "misc",
                    false,
                    "Set to true if you want to start on a 3x3 platform, or false for a tree.");
                if (skyFactory || !platform) {
                    FTBIslands.islandType = config.getString(
                        "Island Type",
                        "misc",
                        "tree",
                        "Set this to the type of platform you want:\n" + "  'grass'     A single grass block.\n"
                            + "  'tree'      A small oak tree on a grass block. This is the standard start.\n"
                            + "  'platform'  A 3x3 platform with a chest.\n"
                            + "  'GoG'       An island similar to Garden of Glass from Botania.\n");
                    config.moveProperty("misc", "Sky Factory", "forRemoval");
                    config.moveProperty("misc", "Platform", "forRemoval");
                    config.removeCategory(config.getCategory("forRemoval"));
                }
            } else {
                FTBIslands.islandType = config.getString(
                    "Island Type",
                    "misc",
                    "tree",
                    "Set this to the type of platform you want:\n" + "  'grass'     A single grass block.\n"
                        + "  'tree'      A small oak tree on a grass block. This is the standard start.\n"
                        + "  'platform'  A 3x3 platform with a chest.\n"
                        + "  'GoG'       An island similar to Garden of Glass from Botania.\n");
                ArrayList<String> types = new ArrayList<>();
                types.add("grass");
                types.add("tree");
                types.add("platform");
                types.add("GoG");

                boolean valid = false;
                for (String s : types) {
                    if (FTBIslands.islandType.equalsIgnoreCase(s)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    logger.warn("Invalid island option detected. Using 'platform' as default.");
                    FTBIslands.islandType = "platform";
                }
            }

            if (config.hasChanged()) {
                config.save();
            }
        }

        @SubscribeEvent
        public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.modID.equalsIgnoreCase(Tags.MODID)) {
                loadConfig();
            }
        }
    }

    private static void convert() throws IOException, ClassNotFoundException {
        if (!oldIslands.exists()) {
            return;
        }
        logger.info("Old islands file found! Trying to convert to new format!");

        FileInputStream fileIn = new FileInputStream(oldIslands);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        HashMap<String, IslandCreator.IslandPos> map = (HashMap<String, IslandCreator.IslandPos>) in.readObject();
        in.close();
        fileIn.close();
        String s = new GsonBuilder().create()
            .toJson(map);

        File newFile = new File(directory, "islands.json");
        FileOutputStream outputStream = new FileOutputStream(newFile);
        FileUtils.writeStringToFile(newFile, s);
        outputStream.close();
        oldIslands.delete();
        logger.info("Conversion completed.");
    }
}
