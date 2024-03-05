package com.alc.archivemanager.controllers;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FileSystemController {

    @GetMapping("/")
    public String index(Model model){
        return "fileSystemPage";
    }
}
