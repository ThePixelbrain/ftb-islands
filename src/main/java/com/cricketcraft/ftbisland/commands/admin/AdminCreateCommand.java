package com.cricketcraft.ftbisland.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class AdminCreateCommand extends CommandLM {

    public AdminCreateCommand() {
        super("island_admin_create", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <islandName> <Owner>", this.commandName);
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames() : super.getTabStrings(ics, args, i);
    }

    @Override
    public Boolean getUsername(String[] args, int i) {
        return i == 1 ? Boolean.TRUE : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException {
        checkArgs(args, 2);
        IslandUtils.StatusCode status = IslandUtils.createIsland(args[0], getPlayer(ics, args[1]), true);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Island %s for user %s successfully created", args[0], args[1]));
    }
}
