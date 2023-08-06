package com.cricketcraft.ftbisland.commands;

import java.util.UUID;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.mojang.authlib.GameProfile;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class ListIslandCommand extends CommandLM {

    public ListIslandCommand() {
        super("island_list", CommandLevel.ALL);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " [<Owner>]";
    }

    @Override
    public Boolean getUsername(String[] args, int i) {
        return i == 0 ? Boolean.TRUE : null;
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        UUID uuid;
        if (strings.length == 0) {
            uuid = getCommandSenderAsPlayer(iCommandSender).getUniqueID();
        } else {
            // This method is needed to also find offline players
            GameProfile profile = MinecraftServer.getServer()
                .func_152358_ax()
                .func_152655_a(strings[0]);
            if (profile == null)
                return error(new ChatComponentText(String.format("Could not find username %s", strings[0])));
            uuid = profile.getId();
        }
        FTBIslands.getIslandStorage()
            .reloadContainer();
        String islands = String.join(
            ", ",
            FTBIslands.getIslandStorage()
                .getContainer()
                .getIslandNames(uuid));
        return new ChatComponentText(islands);
    }
}
