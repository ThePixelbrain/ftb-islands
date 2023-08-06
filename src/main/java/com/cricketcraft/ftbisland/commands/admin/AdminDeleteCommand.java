package com.cricketcraft.ftbisland.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class AdminDeleteCommand extends CommandLM {

    public AdminDeleteCommand() {
        super("island_admin_delete", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <islandName>", this.commandName);
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames() : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        IslandUtils.StatusCode status = IslandUtils.deleteIslandAdmin(strings[0]);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Successfully deleted %s", strings[0]));
    }
}
