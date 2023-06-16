package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class JoinIslandCommand extends CommandLM {

    public JoinIslandCommand() {
        super("island_join", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getAllIslandNames() : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        IslandUtils.joinIsland(strings[0], getCommandSenderAsPlayer(iCommandSender));
        return FTBIslands.mod.chatComponent("cmd.join_success", strings[0]);
    }
}
