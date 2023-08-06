package com.cricketcraft.ftbisland.model;

import java.util.*;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandStorage;

public class IslandContainer {

    private final List<Island> islands;

    private final Map<Integer, Integer> islandsCreated;
    private int fileFormatVersion;

    public IslandContainer(List<Island> islands, Map<Integer, Integer> islandsCreated, int fileFormatVersion) {
        this.islands = islands;
        this.islandsCreated = islandsCreated;
        this.fileFormatVersion = fileFormatVersion;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public int getIslandsCreated(int dim) {
        Integer islandsCreatedInDim = islandsCreated.get(dim);
        return islandsCreatedInDim != null ? islandsCreatedInDim : 0;
    }

    public void incrementIslandsCreated(int dim) {
        islandsCreated.merge(dim, 1, Integer::sum);
    }

    /**
     * Updates islands that were saved with an old version to the latest format
     * Current features:
     * - Add tpPos attribute to all islands
     *
     * @return True if update was done, false if not
     */
    public boolean updateIslandFromOldSave() {
        if (this.fileFormatVersion == IslandStorage.currentFileFormatVersion) return false;

        FTBIslands.logger.info("Old island save detected, updating to new format");
        for (Island island : islands) {
            if (island.getTpPos() == null) {
                island.setTpPos(island.getPos());
            }
        }

        this.fileFormatVersion = IslandStorage.currentFileFormatVersion;
        FTBIslands.logger.info("Successfully updated to new island save format");

        return true;
    }

    public Optional<Island> getIsland(String name) {
        return this.islands.stream()
            .filter(
                island -> island.getName()
                    .equals(name))
            .findFirst();
    }

    public boolean doesIslandExist(String name) {
        return this.getIsland(name)
            .isPresent();
    }

    public long islandsOwnedByUser(UUID owner) {
        return this.islands.stream()
            .filter(
                island -> island.getOwner()
                    .equals(owner))
            .count();
    }

    public Island[] getIslands(UUID owner) {
        return this.islands.stream()
            .filter(
                island -> island.getOwner()
                    .equals(owner))
            .toArray(Island[]::new);
    }

    public String[] getIslandNames(UUID owner) {
        Island[] ownerIslands = this.getIslands(owner);
        if (ownerIslands.length == 0) {
            return new String[0];
        }
        return Arrays.stream(ownerIslands)
            .map(Island::getName)
            .toArray(String[]::new);
    }

    public String[] getIslandNames() {
        return this.islands.stream()
            .map(Island::getName)
            .toArray(String[]::new);
    }
}
