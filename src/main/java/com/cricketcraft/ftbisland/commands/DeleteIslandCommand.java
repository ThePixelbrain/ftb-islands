package com.cricketcraft.ftbisland.commands;

import java.util.UUID;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class DeleteIslandCommand extends CommandLM {

    public DeleteIslandCommand() {
        super("island_delete", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        UUID uuid = getCommandSenderAsPlayer(ics).getUniqueID();
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames(uuid) : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        UUID uuid = getCommandSenderAsPlayer(iCommandSender).getUniqueID();
        IslandUtils.StatusCode status = IslandUtils.deleteIsland(strings[0], uuid);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Successfully deleted %s", strings[0]));
    }
}
