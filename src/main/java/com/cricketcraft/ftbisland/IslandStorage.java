package com.cricketcraft.ftbisland;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.cricketcraft.ftbisland.model.IslandContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class IslandStorage {

    private final File islandFile = new File(FTBIslands.getDirectory(), "islands.json");
    private IslandContainer container;

    public IslandContainer getContainer() {
        return container;
    }

    public void saveContainer() {
        try {
            islandFile.createNewFile();
            try (FileWriter writer = new FileWriter(islandFile)) {
                new GsonBuilder().setPrettyPrinting()
                    .create()
                    .toJson(container, writer);
            }
        } catch (IOException e) {
            FTBIslands.logger.warn("Could not save islands to file");
            e.printStackTrace();
        }
    }

    public void reloadContainer() {
        try {
            IslandContainer containerFromFile = this.getContainerFromFile();
            if (containerFromFile == null) containerFromFile = new IslandContainer(new ArrayList<>(), new HashMap<>());
            this.container = containerFromFile;
        } catch (IOException e) {
            FTBIslands.logger.error("Couldn't get islands from save file");
            e.printStackTrace();
        }
    }

    private IslandContainer getContainerFromFile() throws IOException {
        islandFile.createNewFile();
        try (FileInputStream ignored = new FileInputStream(islandFile)) {
            return new Gson()
                .fromJson(FileUtils.readFileToString(islandFile), new TypeToken<IslandContainer>() {}.getType());
        }
    }
}
