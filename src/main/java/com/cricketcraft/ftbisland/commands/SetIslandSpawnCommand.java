package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class SetIslandSpawnCommand extends CommandLM {

    public SetIslandSpawnCommand() {
        super("island_setspawn", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName> [<X> <Y> <Z>]";
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslands()
            .keySet()
            .toArray(new String[0]) : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        // Allow either just islandName or expect full coordinate set
        if (strings.length != 1) {
            checkArgs(strings, 4);
        }
        FTBIslands.reloadIslands();
        if (!FTBIslands.getIslands()
            .containsKey(strings[0])) {
            return error(FTBIslands.mod.chatComponent("cmd.setspawn_not_exist", strings[0]));
        }
        int x, y, z;
        if (strings.length == 4) {
            x = Integer.parseInt(strings[1]);
            y = Integer.parseInt(strings[2]);
            z = Integer.parseInt(strings[3]);
        } else {
            EntityPlayerMP player = getCommandSenderAsPlayer(iCommandSender);
            x = (int) player.posX;
            y = (int) player.posY;
            z = (int) player.posZ;
        }
        IslandUtils.setSpawnForIsland(strings[0], x, y, z);
        return FTBIslands.mod.chatComponent("cmd.setspawn_success", strings[0], x, y, z);
    }
}
