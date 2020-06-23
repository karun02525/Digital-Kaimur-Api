package com.digital.kaimur.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageProperties {

    private static final String BASE_FOLDER_PATH = System.getProperty("user.dir") + "\\images\\";
    private static final String BASE_PATH_CATEGORY = BASE_FOLDER_PATH + "category\\";
    private static final String BASE_PATH_STORE = BASE_FOLDER_PATH + "store\\";
    private static final String BASE_PATH_PROFILE = BASE_FOLDER_PATH + "profile\\";


    private static StorageProperties instance;

    private Path Root_PATH_CATEGORY = null;
    private Path Root_PATH_STORE = null;
    private Path Root_PATH_PROFILE = null;

    public static synchronized StorageProperties getInstance() {
        if (instance == null) {
            instance = new StorageProperties();
        }
        return instance;
    }

    private StorageProperties() {
        initPath();
    }

    private void initPath() {
        Root_PATH_CATEGORY = Paths.get(BASE_PATH_CATEGORY);
        Root_PATH_STORE = Paths.get(BASE_PATH_STORE);
        Root_PATH_PROFILE = Paths.get(BASE_PATH_PROFILE);

        try {
            Files.createDirectories(Root_PATH_CATEGORY);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

        try {
            Files.createDirectories(Root_PATH_STORE);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

        try {
            Files.createDirectories(Root_PATH_PROFILE);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }

    public Path getCategoryPath() {
        return Root_PATH_CATEGORY;
    }

    public Path getSorePath() {
        return Root_PATH_STORE;
    }

    public Path getProfilePath() {
        return Root_PATH_PROFILE;
    }


}
