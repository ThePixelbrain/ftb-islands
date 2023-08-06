package com.cricketcraft.ftbisland.model;

import java.util.Date;

public class Cooldown {

    private final int limit;
    private final int cooldownSeconds;
    private Date lastAction;
    private int actions = 0;

    public Cooldown(int limit, int cooldownSeconds) {
        this.limit = limit;
        this.cooldownSeconds = cooldownSeconds;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public int getLimit() {
        return limit;
    }

    public void addAction() {
        this.lastAction = new Date();
        this.actions++;
    }

    /**
     * @return Current cooldown or 0 if there is no cooldown
     */
    public int getCooldown() {
        if (this.actions < this.limit) {
            return 0;
        }
        int difference = (int) ((new Date().getTime() - this.lastAction.getTime()) / 1000);
        int cooldown = this.cooldownSeconds - difference;
        if (cooldown <= 0) {
            this.actions = 0;
            return 0;
        }
        return cooldown;
    }
}
