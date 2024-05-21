package com.alc.archivemanager.model;

import com.alc.archivemanager.config.FilePaths;
import com.alc.archivemanager.parsers.IParser;
import com.alc.archivemanager.searchers.SearchProcesses;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SearchResult {
    public String FileName;
    public String Answer;
    public double Character;
    public int PageNumber;
    public String PageText;

    public String getShortFileName(){
        int index = FileName.indexOf("storage") + 8;
        return FileName.substring(index);
    }

    public String getPath(){
        int index = FileName.indexOf(FilePaths.STORAGE_SUFFIX);
        return index != -1 ? FileName.substring(index) : null;
    }

    @Getter
    public double Coincidence;

    public SearchResult(String fileName, String pageText, double coincidence) {
        FileName = fileName;
        PageText = pageText;
        Coincidence = coincidence;
        PageNumber = -666;
    }
}
