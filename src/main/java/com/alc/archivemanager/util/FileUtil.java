package com.alc.archivemanager.util;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.searchers.SearchProcesses;
import com.google.common.io.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static void saveFile(MultipartFile file, String destination) throws IOException {
        File uploadDir = new File(destination);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String uFileName = UUID.randomUUID() + "." + file.getOriginalFilename();
        File uploadedFile = new File(destination + uFileName);
        file.transferTo(uploadedFile);
    }

    public static boolean deleteFile(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Рекурсивное удаление поддиректорий
                        deleteFile(file);
                    } else {
                        delete(file);
                    }
                }
            }
            return delete(directory);

        }
        return false;
    }

    private static boolean delete(File file){
        // Удаление сопутствующего parsed
        File parsed = getParsed(file);
        if(parsed.exists()) parsed.delete();

        // Удаление файла
        return file.delete();
    }

    public static File getParsed(File file){
        return new File(file.getParent() + '\\' + Files.getNameWithoutExtension(file.getPath()) + '.' + SearchProcesses.PARSED);
    }

    public static String getCollisionFilePrefix(String fileName){

        String[] nameParts = Files.getNameWithoutExtension(fileName).split("\\.");

        while (true) {
            if (nameParts.length >= 2) {

                if (nameParts[0].length() != 36) break;

                return nameParts[0] + '.';
            }
            break;
        }
        return "";
    }
}
