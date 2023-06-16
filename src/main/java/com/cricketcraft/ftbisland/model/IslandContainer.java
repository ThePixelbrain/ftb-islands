package com.cricketcraft.ftbisland.model;

import java.util.*;

public class IslandContainer {

    private final List<Island> islands;

    private final Map<Integer, Integer> islandsCreated;

    public IslandContainer(List<Island> islands, Map<Integer, Integer> islandsCreated) {
        this.islands = islands;
        this.islandsCreated = islandsCreated;
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
