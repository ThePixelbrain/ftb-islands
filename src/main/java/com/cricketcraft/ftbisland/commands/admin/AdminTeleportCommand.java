package com.cricketcraft.ftbisland.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class AdminTeleportCommand extends CommandLM {

    public AdminTeleportCommand() {
        super("island_admin_tp", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <IslandName> <Player>", this.commandName);
    }

    @Override
    public Boolean getUsername(String[] args, int i) {
        return i == 1 ? Boolean.TRUE : null;
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames() : super.getTabStrings(ics, args, i);
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 2);
        IslandUtils.StatusCode status = IslandUtils.teleportToIsland(strings[0], getPlayer(iCommandSender, strings[1]));
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(String.format("Teleported %s to island %s", strings[1], strings[0]));
    }
}
