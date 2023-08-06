package com.cricketcraft.ftbisland.commands;

import java.util.UUID;

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

public class SetSpawnIslandCommand extends CommandLM {

    public SetSpawnIslandCommand() {
        super("island_setspawn", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return String.format("/%s <islandName> [<x> <y> <z>]", this.getCommandName());
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
        if (strings == null || (strings.length != 1 && strings.length != 4)) throw new MissingArgsException();
        int x, y, z;
        Integer dimension = null;
        EntityPlayerMP player = getCommandSenderAsPlayer(iCommandSender);
        if (strings.length == 4) {
            x = Integer.parseInt(strings[1]);
            y = Integer.parseInt(strings[2]);
            z = Integer.parseInt(strings[3]);
        } else {
            x = (int) player.posX;
            y = (int) player.posY;
            z = (int) player.posZ;
            dimension = player.dimension;
        }

        IslandUtils.StatusCode status = IslandUtils
            .setIslandTpPos(strings[0], player.getUniqueID(), x, y, z, dimension, false);
        if (status != IslandUtils.StatusCode.SUCCESS) {
            return error(new ChatComponentText(status.getMessage()));
        }
        return new ChatComponentText(
            String.format("Successfully moved spawnpoint of island %s to %d %d %d", strings[0], x, y, z));
    }
}
