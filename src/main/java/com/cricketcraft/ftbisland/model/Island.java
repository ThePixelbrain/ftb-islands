package com.cricketcraft.ftbisland.model;

import java.util.UUID;

public class Island {

    private String name;
    private UUID owner;
    private final Position pos;
    private Position tpPos;

    public Island(String name, UUID owner, Position pos, Position tpPos) {
        this.name = name;
        this.owner = owner;
        this.pos = pos;
        this.tpPos = tpPos;
    }

    public Island(String name, UUID owner, Position pos) {
        this(name, owner, pos, pos);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Position getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getTpPos() {
        return tpPos;
    }

    public void setTpPos(Position tpPos) {
        this.tpPos = tpPos;
    }

    public static class Position {

        private final int x;
        private final int y;
        private final int z;
        private final int dim;

        public Position(int x, int y, int z, int dim) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dim = dim;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public int getDim() {
            return dim;
        }
    }
}
