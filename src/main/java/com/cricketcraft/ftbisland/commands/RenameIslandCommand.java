package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class RenameIslandCommand extends CommandLM {

    public RenameIslandCommand() {
        super("island_rename", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <OldName> <NewName>";
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslands()
            .keySet()
            .toArray(new String[0]) : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 2);
        FTBIslands.reloadIslands();
        if (!FTBIslands.getIslands()
            .containsKey(strings[0])) {
            return error(FTBIslands.mod.chatComponent("cmd.rename_old_not_exist", strings[0]));
        }
        if (FTBIslands.getIslands()
            .containsKey(strings[1])) {
            return error(FTBIslands.mod.chatComponent("cmd.rename_new_exists", strings[1]));
        }
        IslandUtils.renameIsland(strings[0], strings[1]);
        return FTBIslands.mod.chatComponent("cmd.rename_success", strings[0], strings[1]);
    }
}
