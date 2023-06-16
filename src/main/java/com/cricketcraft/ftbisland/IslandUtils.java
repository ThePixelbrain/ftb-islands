package com.cricketcraft.ftbisland;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import com.cricketcraft.ftbisland.model.Island;

public class IslandUtils {

    public static void createIsland(World world, String islandName, EntityPlayer owner) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Pair<Integer, Integer> spiralLoc = calculateSpiral(
            FTBIslands.getIslandStorage()
                .getContainer()
                .getIslandsCreated(owner.worldObj.provider.dimensionId));
        IslandCreator.spawnIslandAt(
            world,
            spiralLoc.getLeft() * 512 + 256,
            64,
            spiralLoc.getRight() * 512 + 256,
            islandName,
            owner);
    }

    public static void renameIsland(Island island, String newName) {
        island.setName(newName);
        FTBIslands.getIslandStorage()
            .saveContainer();
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        FTBIslands.getIslandStorage()
            .reloadContainer();
        Optional<Island> island = FTBIslands.getIslandStorage()
            .getContainer()
            .getIslandByName(islandName);
        if (island.isPresent()) {
            Island.Position pos = island.get()
                .getPos();
            if (player.dimension != pos.getDim()) {
                player.travelToDimension(pos.getDim());
            }
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            int height = FTBIslands.islandType.equalsIgnoreCase("tree") ? 6 : 2;
            double xAndZ = FTBIslands.islandType.equalsIgnoreCase("grass") ? 0.5 : 1.5;
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                playerMP.setPositionAndUpdate(x + xAndZ, y + height, z + xAndZ);
                // ChunkCoordinates chunk = new ChunkCoordinates(x, y, z);
                // playerMP.setSpawnChunk(chunk, true);
            }
        } else {
            player.addChatComponentMessage(new ChatComponentText("Island does not exist!"));
        }
    }

    public static void deleteIsland(Island island) {
        FTBIslands.getIslandStorage()
            .getContainer()
            .getIslands()
            .remove(island);
        FTBIslands.getIslandStorage()
            .saveContainer();
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
