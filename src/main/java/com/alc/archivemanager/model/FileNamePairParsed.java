package com.alc.archivemanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileNamePairParsed {
    private String fileName;
    private String parsedFileName;

    public FileNamePairParsed(String fileName){
        this.fileName = fileName;
    }

    public String getNameForParse(){
        return parsedFileName != null ? parsedFileName : fileName;
    }
}
