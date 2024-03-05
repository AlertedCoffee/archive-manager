package com.alc.archivemanager.util;

import com.alc.archivemanager.model.FileSystemItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<FileSystemItem> getFiles(String mainPath){
        File dir = new File(mainPath);
        File[] files = dir.listFiles();

        List<FileSystemItem> result = new ArrayList<>();

        if(files != null) {
            for (File file : files) {
                result.add(new FileSystemItem(file));
            }
        }

        return result;
    }
}
