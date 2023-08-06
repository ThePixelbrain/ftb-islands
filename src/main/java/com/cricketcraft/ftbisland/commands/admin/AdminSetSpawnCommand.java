package com.cricketcraft.ftbisland.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.api.cmd.MissingArgsException;

public class AdminSetSpawnCommand extends CommandLM {

    public AdminSetSpawnCommand() {
        super("island_admin_setspawn", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <islandName> [<x> <y> <z>]", this.getCommandName());
    }

    @Override
    public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException {
        return i == 0 ? FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandNames() : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (strings == null || (strings.length != 1 && strings.length != 4)) throw new MissingArgsException();
        int x, y, z;
        Integer dimension = null;
        if (strings.length == 4) {
            x = Integer.parseInt(strings[1]);
            y = Integer.parseInt(strings[2]);
            z = Integer.parseInt(strings[3]);
        } else {
            EntityPlayerMP player = getCommandSenderAsPlayer(iCommandSender);
            x = (int) player.posX;
            y = (int) player.posY;
            z = (int) player.posZ;
            dimension = player.dimension;
        }

        IslandUtils.StatusCode status = IslandUtils.setIslandTpPos(strings[0], null, x, y, z, dimension, true);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(
            String.format("Successfully moved spawnpoint of island %s to %d %d %d", strings[0], x, y, z));
    }
}
