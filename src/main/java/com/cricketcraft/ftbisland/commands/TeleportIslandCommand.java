package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class TeleportIslandCommand extends CommandLM {

    public TeleportIslandCommand() {
        super("island_tp", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
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
        IslandUtils.StatusCode status = IslandUtils
            .teleportToIsland(strings[0], getCommandSenderAsPlayer(iCommandSender));
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Teleported to island %s", strings[0]));
    }
}
