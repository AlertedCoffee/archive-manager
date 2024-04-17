package com.alc.archivemanager.util;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.searchers.SearchProcesses;
import com.google.common.io.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {

    private static final String TRASH_FOLDER = "C:/WebPractice/archive-manager/src/main/resources/TrashFolder";
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

    public static boolean moveToTrashFile(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Рекурсивное удаление поддиректорий
                        moveToTrashFile(file);
                    } else {
                        File parsed = getParsed(file);
                        if(parsed.exists())
                            moveToTrash(parsed);
                        moveToTrash(file);
                    }
                }
            }
            return moveToTrash(directory);

        }
        return false;
    }

    private static boolean moveToTrash(File file){
        File newLocation = new File(TRASH_FOLDER, file.getName());
        try {
            java.nio.file.Files.move(file.toPath(), newLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);


            File metadataFile = new File(TRASH_FOLDER, file.getName() + ".metadata");
            metadataFile.createNewFile();
            java.nio.file.Files.write(metadataFile.toPath(), file.getAbsolutePath().getBytes());

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static boolean recoverFile(File file){
        File parsed = getParsed(file);
        if(parsed.exists())
            recoverFromTrash(parsed);

        return recoverFromTrash(file);
    }

    private static boolean recoverFromTrash(File file){

        File metadataFile = new File(file.getParent() + "\\" + file.getName() + ".metadata");

        try {
            // Восстановление файла
            String previousLocation = java.nio.file.Files.readString(metadataFile.toPath());
            java.nio.file.Files.move(file.toPath(), new File(previousLocation).toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Удаление метаданных
            metadataFile.delete();
        } catch (IOException e) {
            return false;
        }


        return true;
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
