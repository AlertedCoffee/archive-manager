package com.alc.archivemanager.util;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.model.TrashFileItem;
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

    public static List<TrashFileItem> getTrashedFiles(){
        File dir = new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX);
        File[] files = dir.listFiles();

        List<TrashFileItem> result = new ArrayList<>();

        if(files != null) {
            for (File file : files) {
                try {
                    result.add(new TrashFileItem(file));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
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
        File newLocation = new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX, file.getName());
        try {
            java.nio.file.Files.move(file.toPath(), newLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);


            File metadataFile = getMetadata(file);
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

        File metadataFile = getMetadata(file);

        try {
            // Восстановление файла
            String previousLocation = java.nio.file.Files.readString(metadataFile.toPath());

            File targetFile = new File(previousLocation);
            Files.createParentDirs(targetFile);

            try {
                java.nio.file.Files.move(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (java.nio.file.DirectoryNotEmptyException e){
                file.delete();
            }
            // Удаление метаданных
            metadataFile.delete();
        } catch (IOException e) {
            return false;
        }


        return true;
    }

    public static boolean deleteFileFromTrash(File file) {
        File metadataFile = getMetadata(file);

        return (file.delete() && metadataFile.delete());
    }

    public static File getParsed(File file){
        return new File(file.getParent() + '\\' + Files.getNameWithoutExtension(file.getPath()) + '.' + SearchProcesses.PARSED);
    }

    public static File getMetadata(File file){
        return new File(FilePaths.MAIN_PATH + FilePaths.TRASH_SUFFIX, file.getName() + ".metadata");
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
