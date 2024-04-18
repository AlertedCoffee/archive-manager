package com.alc.archivemanager.model;

import com.alc.archivemanager.util.FilePaths;
import com.alc.archivemanager.util.FileUtil;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class TrashFileItem {
    private final String path;
    private final FileType fileType;
    private final String parent;

    public TrashFileItem(String path, FileType fileType, String parent) {
        this.path = path;
        this.fileType = fileType;
        this.parent = parent;
    }

    public TrashFileItem(File file) throws IOException {
        String path = file.getPath();
        this.path = path.substring(path.lastIndexOf(FilePaths.TRASH_SUFFIX));

        String extension = Files.getFileExtension(file.getName());
        this.fileType = switch (extension){
            case "" -> FileType.DIR;
            case "parsed" -> FileType.PARSED;
            case "metadata" -> FileType.METADATA;
            default -> FileType.FILE;
        };

        if(fileType != FileType.METADATA) {
            File metadataFile = FileUtil.getMetadata(file);
            String previousLocation = java.nio.file.Files.readString(metadataFile.toPath());
            this.parent = previousLocation.substring(previousLocation.lastIndexOf(FilePaths.STORAGE_SUFFIX));
        }
        else this.parent = null;
    }

    public String getName() {
        String[] pathParts = path.split("\\.");

        while (true) {
            if (pathParts.length >= 3) {
                if (pathParts[0].length() != 49) break;

                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < pathParts.length; i++) {
                    builder.append(pathParts[i]);
                    if (i < pathParts.length - 1) builder.append('.');
                }

                return builder.toString();
            }
            break;
        }
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
