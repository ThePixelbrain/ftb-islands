package com.cricketcraft.ftbisland;

import java.util.*;

import net.minecraft.entity.player.EntityPlayerMP;

import org.apache.commons.lang3.tuple.Pair;

import com.cricketcraft.ftbisland.model.Cooldown;
import com.cricketcraft.ftbisland.model.Island;

public class IslandUtils {

    private static final Map<UUID, Cooldown> deletionCooldown = new HashMap<>();

    public enum StatusCode {

        SUCCESS("Success!"),
        FAIL_EXIST("Island %s already exists!"),
        FAIL_NOT_EXIST("Island %s doesn't exist!"),
        FAIL_MAX_ISLANDS("A player can only have %d island(s)!"),
        FAIL_WRONG_OWNER("Island %s is owned by a different player!"),
        FAIL_COOLDOWN("You have to wait %d minutes until you can execute this action again!"),
        FAIL_WRONG_DIMENSION("You are in the wrong dimension, you need to be in dimension %d!"),
        FAIL_DISTANCE_EXCEEDED("You are too far away from the original island position!");

        private final String message;
        private Object[] args;

        StatusCode(String message) {
            this.message = message;
        }

        public StatusCode setArgs(Object... args) {
            this.args = args;
            return this;
        }

        public String getMessage() {
            return String.format(message, args);
        }
    }

    public static StatusCode createIsland(String islandName, EntityPlayerMP owner, boolean adminSender) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        if (FTBIslands.getIslandStorage()
            .getContainer()
            .doesIslandExist(islandName)) {
            return StatusCode.FAIL_EXIST.setArgs(islandName);
        }
        if (!adminSender && FTBIslands.getIslandStorage()
            .getContainer()
            .islandsOwnedByUser(owner.getUniqueID()) >= Config.maxIslandsPerPlayer) {
            return StatusCode.FAIL_MAX_ISLANDS.setArgs(Config.maxIslandsPerPlayer);
        }
        Pair<Integer, Integer> spiralLoc = calculateSpiral(
            FTBIslands.getIslandStorage()
                .getContainer()
                .getIslandsCreated(owner.worldObj.provider.dimensionId));
        IslandCreator
            .spawnIslandAt(spiralLoc.getLeft() * 512 + 256, 64, spiralLoc.getRight() * 512 + 256, islandName, owner);
        return StatusCode.SUCCESS;
    }

    public static StatusCode renameIsland(String oldName, String newName, UUID owner, boolean adminSender) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(oldName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(oldName);
        }
        if (!adminSender && !island.get()
            .getOwner()
            .equals(owner)) {
            return StatusCode.FAIL_WRONG_OWNER.setArgs(oldName);
        }
        if (FTBIslands.getIslandStorage()
            .getContainer()
            .doesIslandExist(newName)) {
            return StatusCode.FAIL_EXIST.setArgs(newName);
        }
        island.get()
            .setName(newName);
        FTBIslands.getIslandStorage()
            .saveContainer();
        return StatusCode.SUCCESS;
    }

    public static StatusCode teleportToIsland(String islandName, EntityPlayerMP player) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(islandName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(islandName);
        }
        Island.Position pos = island.get()
            .getTpPos();
        if (player.dimension != pos.getDim()) {
            player.travelToDimension(pos.getDim());
        }
        player.setPositionAndUpdate(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        return StatusCode.SUCCESS;
    }

    public static StatusCode deleteIslandAdmin(String islandName) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(islandName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(islandName);
        }

        FTBIslands.getIslandStorage()
            .getContainer()
            .getIslands()
            .remove(island.get());
        FTBIslands.getIslandStorage()
            .saveContainer();

        return StatusCode.SUCCESS;
    }

    public static StatusCode deleteIsland(String islandName, UUID owner) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(islandName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(islandName);
        }
        if (!island.get()
            .getOwner()
            .equals(owner)) {
            return StatusCode.FAIL_WRONG_OWNER.setArgs(islandName);
        }
        Cooldown cooldown = deletionCooldown.get(owner);
        if (cooldown == null) {
            cooldown = new Cooldown(Config.maxIslandsPerPlayer, Config.islandDeletionCooldown * 60);
            deletionCooldown.put(owner, cooldown);
        }
        if (cooldown.getCooldown() != 0) {
            return StatusCode.FAIL_COOLDOWN.setArgs(cooldown.getCooldown() / 60);
        }
        FTBIslands.getIslandStorage()
            .getContainer()
            .getIslands()
            .remove(island.get());
        FTBIslands.getIslandStorage()
            .saveContainer();
        cooldown.addAction();
        return StatusCode.SUCCESS;
    }

    public static StatusCode setIslandTpPos(String islandName, UUID owner, int x, int y, int z, Integer dimension,
        boolean adminSender) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(islandName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(islandName);
        }
        if (!adminSender && !island.get()
            .getOwner()
            .equals(owner)) {
            return StatusCode.FAIL_WRONG_OWNER.setArgs(islandName);
        }
        Island.Position originalPos = island.get()
            .getPos();
        if (dimension != null && dimension != originalPos.getDim()) {
            return StatusCode.FAIL_WRONG_DIMENSION.setArgs(originalPos.getDim());
        }
        if (Math.abs(originalPos.getX() - x) >= 128 || Math.abs(originalPos.getZ() - z) >= 128) {
            return StatusCode.FAIL_DISTANCE_EXCEEDED;
        }

        island.get()
            .setTpPos(new Island.Position(x, y, z, originalPos.getDim()));
        FTBIslands.getIslandStorage()
            .saveContainer();

        return StatusCode.SUCCESS;
    }

    public static StatusCode changeIslandOwner(String islandName, UUID newOwner, UUID cmdSender, boolean adminSender) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIsland(islandName);
        if (!island.isPresent()) {
            return StatusCode.FAIL_NOT_EXIST.setArgs(islandName);
        }
        if (!adminSender && !island.get()
            .getOwner()
            .equals(cmdSender)) {
            return StatusCode.FAIL_WRONG_OWNER.setArgs(islandName);
        }
        island.get()
            .setOwner(newOwner);
        FTBIslands.getIslandStorage()
            .saveContainer();

        return StatusCode.SUCCESS;
    }

    private static Pair<Integer, Integer> calculateSpiral(int index) {
        if (index == 0) {
            return Pair.of(0, 0);
        }

        // current position (x, z) and how much of current segment we passed
        int x = 0;
        int z = 0;

        int dx = 0;
        int dz = 1;
        int segmentLength = 1;
        int segmentPassed = 0;

        for (int n = 0; n < index; n++) {
            x += dx;
            z += dz;
            segmentPassed++;

            if (segmentPassed == segmentLength) {
                segmentPassed = 0;

                // 'rotate' directions
                int buffer = dz;
                dz = -dx;
                dx = buffer;

                // increase segment length if necessary
                if (dx == 0) {
                    segmentLength++;
                }
            }
        }

        return Pair.of(x, z);
    }
}
