package com.alc.archivemanager.model;

public class SearchResult {
    public String Answer;
    public double Character;
    public double Coincidence;

    public SearchResult(String answer, double character, double coincidence) {
        Answer = answer;
        Character = character;
        Coincidence = coincidence;
    }
}
