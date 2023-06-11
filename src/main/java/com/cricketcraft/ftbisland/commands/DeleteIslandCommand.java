package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class DeleteIslandCommand extends CommandLM {

    public DeleteIslandCommand() {
        super("island_delete", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslands()
            .keySet()
            .toArray(new String[0]) : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        FTBIslands.reloadIslands();
        if (!FTBIslands.getIslands()
            .containsKey(strings[0])) {
            return error(FTBIslands.mod.chatComponent("cmd.delete_not_exist", strings[0]));
        }
        IslandUtils.deleteIsland(strings[0]);
        return FTBIslands.mod.chatComponent("cmd.delete_success", strings[0]);
    }
}
