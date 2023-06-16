package com.cricketcraft.ftbisland.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Island> getIslandByName(String name) {
        return this.islands.stream()
            .filter(
                island -> island.getName()
                    .equals(name))
            .findFirst();
    }

    public boolean doesIslandExist(String name) {
        return this.getIslandByName(name)
            .isPresent();
    }

    public long islandsOwnedByUser(UUID owner) {
        return this.islands.stream()
            .filter(
                island -> island.getOwner()
                    .equals(owner))
            .count();
    }

    public String[] getAllIslandNames() {
        return this.islands.stream()
            .map(Island::getName)
            .toArray(String[]::new);
    }
}
