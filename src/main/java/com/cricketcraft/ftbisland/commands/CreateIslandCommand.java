package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;

public class CreateIslandCommand extends CommandLM {

    public CreateIslandCommand() {
        super("island_create", CommandLevel.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender ics) {
        return "/" + this.getCommandName() + " <IslandName>";
    }

    @Override
    public IChatComponent onCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        checkArgs(strings, 1);
        FTBIslands.getIslandStorage()
            .reloadContainer();
        if (FTBIslands.getIslandStorage()
            .getContainer()
            .doesIslandExist(strings[0])) {
            return error(FTBIslands.mod.chatComponent("cmd.create_exists", strings[0]));
        }
        IslandUtils.createIsland(iCommandSender.getEntityWorld(), strings[0], getCommandSenderAsPlayer(iCommandSender));
        return FTBIslands.mod.chatComponent("cmd.create_success", strings[0]);
    }
}
