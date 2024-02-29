package com.alc.archivemanager.parsers;

import com.alc.archivemanager.searchers.SearchProcesses;

import java.io.BufferedWriter;
import java.io.FileWriter;

public interface IParser {
    public String parse(String filePath) throws Exception;
    public static void saveParsed(String text, String filePath) throws Exception{
        int dotIndex = filePath.lastIndexOf('.');
        filePath = filePath.substring(0, dotIndex) + SearchProcesses.PARSED;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(text);
        }
    }
}
