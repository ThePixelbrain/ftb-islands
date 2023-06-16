package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class CreateIslandCommand extends CommandLM {

    public CreateIslandCommand() {
        super("island_create", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        IslandUtils.StatusCode status = IslandUtils
            .createIsland(strings[0], getCommandSenderAsPlayer(iCommandSender), false);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Island %s successfully created", strings[0]));
    }
}
