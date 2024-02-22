package com.alc.archivemanager.model;

public class SearchResultModel {
    public String FileName;
    public String Answer;
    public double Character;

    public String PageText;

    public double getCoincidence() {
        return Coincidence;
    }

    public double Coincidence;


    public SearchResultModel(String fileName, String answer, double character, String pageText, double coincidence) {
        FileName = fileName;
        Answer = answer;
        Character = character;
        PageText = pageText;
        Coincidence = coincidence;
    }
}
