package com.alc.archivemanager.model;

import java.io.File;

public class FileSystemItem {
    private final String path;
    private final FileType fileType;

    private final String parent;

    public FileSystemItem(String path, FileType fileType, String parent) {
        this.path = path;
        this.fileType = fileType;
        this.parent = parent;
    }

    public FileSystemItem(File file) {
        String path = file.getPath();
        this.path = path;

        int dotIndex = path.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "" : path.substring(dotIndex);
        this.fileType = switch (extension){
            case "" -> FileType.DIR;
            case ".parsed" -> FileType.PARSED;
            default -> FileType.FILE;
        };

        String parent = file.getParent();
        if(!parent.substring(parent.lastIndexOf('\\')).equals("\\storage")) this.parent = parent;
        else this.parent = null;
    }

    public String getName() {
        return path.substring(path.lastIndexOf("\\") + 1);
    }

    public String getPath() {
        return path;
    }

    public String getParent() {
        return parent;
    }

    public FileType getFileType() {
        return fileType;
    }
}
