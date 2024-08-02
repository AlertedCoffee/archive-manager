package com.alc.archivemanager.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePaths {
    @Value("${resource.path}")
    private String mainPath;
    public static String MAIN_PATH;
    public static final String STORAGE_SUFFIX = "\\storage\\";
    public static final String TRASH_SUFFIX = "\\trashFolder\\";

    @PostConstruct
    public void init() {
        MAIN_PATH = this.mainPath;
    }
}
