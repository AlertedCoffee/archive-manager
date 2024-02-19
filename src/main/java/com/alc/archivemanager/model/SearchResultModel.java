package com.alc.archivemanager.model;

public class SearchResultModel {
    public String FileName;
    public String Answer;
    public double Character;

    public double getCoincidence() {
        return Coincidence;
    }

    public double Coincidence;

    public SearchResultModel(String fileName, String answer, double character, double coincidence) {
        FileName = fileName;
        Answer = answer;
        Character = character;
        Coincidence = coincidence;
    }
}
