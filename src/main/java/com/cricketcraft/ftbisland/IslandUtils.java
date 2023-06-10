package com.cricketcraft.ftbisland;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class IslandUtils {

    public static boolean createIsland(World world, String playerName, EntityPlayer player) {
        IslandCreator.reloadIslands();
        if (IslandCreator.islandLocations == null) {
            FTBIslands.logger.info("Island locations are null?? Empty possibly.");
            return false;
        }
        IslandCreator.IslandPos pos = FTBIslands.islandLoc.get(IslandCreator.islandLocations.size() + 1);
        IslandCreator
            .spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(), playerName, (player != null ? player : null));
        return true;
    }

    public static void renameIsland(String oldName, String newName) {
        IslandCreator.IslandPos pos = IslandCreator.islandLocations.get(oldName);
        IslandCreator.islandLocations.remove(oldName);
        IslandCreator.islandLocations.put(newName, pos);
        IslandCreator.save();
    }

    public static void setSpawnForIsland(String s, int x, int y, int z) {
        IslandCreator.IslandPos pos = new IslandCreator.IslandPos(x, y, z);
        IslandCreator.islandLocations.remove(s);
        IslandCreator.islandLocations.put(s, pos);
        IslandCreator.save();
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        if (player == null) {
            FTBIslands.logger.info("The join command must be run in game.");
        } else {
            IslandCreator.reloadIslands();
            if (IslandCreator.islandLocations.containsKey(islandName)) {
                IslandCreator.IslandPos pos = new IslandCreator.IslandPos(0, 60, 0);
                for (String key : IslandCreator.islandLocations.keySet()) {
                    if (key.equalsIgnoreCase(islandName)) {
                        pos = IslandCreator.islandLocations.get(key);
                    }
                }
                if (player.dimension != 0) {
                    player.travelToDimension(0);
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
    }

    public static void deleteIsland(String islandName) {
        IslandCreator.islandLocations.remove(islandName);
        IslandCreator.save();
    }
}
