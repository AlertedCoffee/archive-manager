package com.alc.archivemanager.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DeepPavlovApiRequest {
    @SerializedName("context_raw")
    private List<String> contextRaw;

    @SerializedName("question_raw")
    private List<String> questionRaw;
}
