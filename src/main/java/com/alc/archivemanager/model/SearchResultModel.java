package com.alc.archivemanager.model;

public class SearchResultModel {
    public String Answer;
    public double Character;
    public double Coincidence;

    public SearchResultModel(String answer, double character, double coincidence) {
        Answer = answer;
        Character = character;
        Coincidence = coincidence;
    }
}
