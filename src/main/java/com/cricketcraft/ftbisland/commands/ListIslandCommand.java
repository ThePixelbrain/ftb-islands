package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class ListIslandCommand extends CommandLM {

    public ListIslandCommand() {
        super("island_list", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName();
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        for (String name : FTBIslands.getIslandStorage()
            .getContainer()
            .getAllIslandNames()) {
            iCommandSender.addChatMessage(new ChatComponentText(name));
        }
        return null;
    }
}
