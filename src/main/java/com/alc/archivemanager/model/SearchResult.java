package com.alc.archivemanager.model;

public class SearchResult {
    public String FileName;
    public String Answer;
    public double Character;

    public String PageText;

    public double getCoincidence() {
        return Coincidence;
    }
    public String getShortFileName(){
        int index = FileName.indexOf("storage") + 8;
        return FileName.substring(index, FileName.lastIndexOf('.'));
    }

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
