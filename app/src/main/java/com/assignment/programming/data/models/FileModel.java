package com.assignment.programming.data.models;

import androidx.annotation.NonNull;

import java.io.File;

public class FileModel {
    private final String name;
    private final String path;
    private final long lastModified;
    private String ext = null;

    public FileModel(File file) {
        this.name = file.getName();
        this.path = file.getPath();
        this.lastModified = file.lastModified();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getExt() {
        if (ext == null) {
            String ext = "";
            int i = name.lastIndexOf('.');
            if (i > 0) {
                ext = name.substring(i + 1);
            }
            this.ext = ext;
        }
        return ext;
    }

    @NonNull
    @Override
    public String toString() {
        return "FileModel{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
