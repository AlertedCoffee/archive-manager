package com.alc.archivemanager.model;

import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor
public class FileSystemItem {
    private final String path;
    private final FileType fileType;
    private final String parent;

    public FileSystemItem(File file) {
        String path = file.getPath();
        this.path = path.substring(path.lastIndexOf("\\storage"));

        String extension = Files.getFileExtension(file.getName());
        this.fileType = switch (extension){
            case "" -> FileType.DIR;
            case "parsed" -> FileType.PARSED;
            default -> FileType.FILE;
        };

        String parent = file.getParent();
        parent = parent.substring(parent.lastIndexOf("\\storage"));
        if(!parent.substring(parent.lastIndexOf('\\')).equals("\\storage")) this.parent = parent;
        else this.parent = null;
    }

    public String getName() {
        String[] pathParts = path.substring(path.lastIndexOf('\\')).split("\\.");

        while (true) {
            if (pathParts.length >= 3) {
                int test = pathParts[0].length();
                if (pathParts[0].length() != 37) break;

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

}
