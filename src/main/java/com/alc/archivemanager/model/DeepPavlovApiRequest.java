package com.alc.archivemanager.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeepPavlovApiRequest {
    @SerializedName("context_raw")
    private List<String> contextRaw;

    @SerializedName("question_raw")
    private List<String> questionRaw;

    // Конструктор
    public DeepPavlovApiRequest(List<String> contextRaw, List<String> questionRaw) {
        this.contextRaw = contextRaw;
        this.questionRaw = questionRaw;
    }

    // Геттеры и сеттеры
    public List<String> getContextRaw() {
        return contextRaw;
    }

    public void setContextRaw(List<String> contextRaw) {
        this.contextRaw = contextRaw;
    }

    public List<String> getQuestionRaw() {
        return questionRaw;
    }

    public void setQuestionRaw(List<String> questionRaw) {
        this.questionRaw = questionRaw;
    }
}
