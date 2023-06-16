package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class TeleportIslandCommand extends CommandLM {

    public TeleportIslandCommand() {
        super("island_teleport", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName> <Player>";
    }

    @Override
    public Boolean getUsername(String[] args, int i) {
        return i == 1 ? Boolean.TRUE : null;
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getAllIslandNames() : super.getTabStrings(ics, args, i);
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 2);
        IslandUtils.joinIsland(strings[0], getPlayer(iCommandSender, strings[1]));
        return FTBIslands.mod.chatComponent("cmd.tp_island", strings[0]);
    }
}
