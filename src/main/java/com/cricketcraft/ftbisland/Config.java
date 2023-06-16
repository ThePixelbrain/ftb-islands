package com.cricketcraft.ftbisland;

import java.io.File;
import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {

    private static Configuration config;
    public static int maxIslandsPerPlayer = 1;

    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file);
            loadConfig();
        }
    }

    private static void loadConfig() {
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
                FTBIslands.logger.warn("Invalid island option detected. Using 'platform' as default.");
                FTBIslands.islandType = "platform";
            }
        }

        maxIslandsPerPlayer = config.getInt(
            "Max Islands Per Player",
            "misc",
            maxIslandsPerPlayer,
            0,
            1000,
            "Maximum amount of islands a player can own");

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
