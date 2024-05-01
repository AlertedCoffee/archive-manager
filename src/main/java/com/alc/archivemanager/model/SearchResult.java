package com.alc.archivemanager.model;

import com.alc.archivemanager.config.FilePaths;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.SearchProcesses;
import com.google.common.io.Files;
import lombok.Getter;

public class SearchResult {
    public String FileName;
    public String Answer;
    public double Character;

    public String PageText;

    public String getShortFileName(){
        int index = FileName.indexOf("storage") + 8;
        return FileName.substring(index, FileName.lastIndexOf('.'));
    }

    public String getPath(){
        if(Files.getFileExtension(FileName).equals(SearchProcesses.PDF)) {
            int index = FileName.indexOf(FilePaths.STORAGE_SUFFIX);
            return index != -1 ? FileName.substring(index) : null;
        }
        else return null;
    }

    @Getter
    public double Coincidence;


    public SearchResult(String fileName, String answer, double character, String pageText, double coincidence) {
        FileName = fileName;
        Answer = answer;
        Character = character;
        PageText = pageText;
        Coincidence = coincidence;
    }

    public SearchResult(String fileName, String pageText, double coincidence) {
        FileName = fileName;
        PageText = pageText;
        Coincidence = coincidence;
    }
}
