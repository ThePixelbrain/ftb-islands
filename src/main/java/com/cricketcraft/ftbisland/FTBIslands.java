package com.cricketcraft.ftbisland;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cricketcraft.ftbisland.commands.*;
import com.cricketcraft.ftbisland.commands.admin.AdminCreateCommand;
import com.cricketcraft.ftbisland.commands.admin.AdminDeleteCommand;
import com.cricketcraft.ftbisland.commands.admin.AdminRenameCommand;
import com.cricketcraft.ftbisland.commands.admin.AdminTeleportCommand;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ftb.lib.LMMod;

@Mod(
    modid = Tags.MODID,
    name = Tags.MODNAME,
    version = Tags.VERSION,
    dependencies = "required-after:FTBL",
    acceptableRemoteVersions = "*")
public class FTBIslands {

    public static Logger logger;
    public static String islandType;
    public static LMMod mod;
    private static File directory;

    private static IslandStorage islandStorage;

    @Mod.EventHandler
    public void serverLoading(FMLServerStartingEvent event) {
        logger.info("Registering commands.");
        event.registerServerCommand(new CreateIslandCommand());
        event.registerServerCommand(new DeleteIslandCommand());
        event.registerServerCommand(new TeleportIslandCommand());
        event.registerServerCommand(new ListIslandCommand());
        event.registerServerCommand(new RenameIslandCommand());
        event.registerServerCommand(new AdminCreateCommand());
        event.registerServerCommand(new AdminDeleteCommand());
        event.registerServerCommand(new AdminRenameCommand());
        event.registerServerCommand(new AdminTeleportCommand());
        logger.info("Finished registering commands.");
        islandStorage.reloadContainer();
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        Config.init(new File(event.getModConfigurationDirectory(), "ftbi/FTB_Islands.cfg"));
        logger = LogManager.getLogger("FTBI");
        mod = LMMod.create("FTBI");
        File dir = event.getModConfigurationDirectory();
        directory = new File(dir.getParentFile(), "local");
        islandStorage = new IslandStorage();
    }

    public static File getDirectory() {
        return directory;
    }

    public static IslandStorage getIslandStorage() {
        return islandStorage;
    }
}
