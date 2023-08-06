package com.cricketcraft.ftbisland.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class AdminSetOwnerCommand extends CommandLM {

    public AdminSetOwnerCommand() {
        super("island_admin_setowner", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <IslandName> <newOwner>", this.getCommandName());
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
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 2);
        IslandUtils.StatusCode status = IslandUtils
            .changeIslandOwner(strings[0], getPlayer(iCommandSender, strings[1]).getUniqueID(), null, true);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(
            String.format("Successfully transferred ownership of island %s to %s", strings[0], strings[1]));
    }
}
