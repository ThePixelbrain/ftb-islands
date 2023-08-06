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

public class SetOwnerIslandCommand extends CommandLM {

    public SetOwnerIslandCommand() {
        super("island_setowner", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <IslandName> <newOwner>", this.getCommandName());
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        UUID uuid = getCommandSenderAsPlayer(ics).getUniqueID();
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames(uuid) : super.getTabStrings(ics, args, i);
    }

    @Override
    public Boolean getUsername(String[] args, int i) {
        return i == 1 ? Boolean.TRUE : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 2);
        IslandUtils.StatusCode status = IslandUtils.changeIslandOwner(
            strings[0],
            getPlayer(iCommandSender, strings[1]).getUniqueID(),
            getCommandSenderAsPlayer(iCommandSender).getUniqueID(),
            false);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(
            String.format("Successfully transferred ownership of island %s to %s", strings[0], strings[1]));
    }
}
