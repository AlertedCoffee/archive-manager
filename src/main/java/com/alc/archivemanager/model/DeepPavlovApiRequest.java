package com.alc.archivemanager.model;

import java.util.List;

public class DeepPavlovApiRequest {
    private List<String> context_raw;
    private List<String> question_raw;

    // Конструктор
    public DeepPavlovApiRequest(List<String> context_raw, List<String> question_raw) {
        this.context_raw = context_raw;
        this.question_raw = question_raw;
    }

    // Геттеры и сеттеры
    public List<String> getContext_raw() {
        return context_raw;
    }

    public void setContext_raw(List<String> context_raw) {
        this.context_raw = context_raw;
    }

    public List<String> getQuestion_raw() {
        return question_raw;
    }

    public void setQuestion_raw(List<String> question_raw) {
        this.question_raw = question_raw;
    }
}
