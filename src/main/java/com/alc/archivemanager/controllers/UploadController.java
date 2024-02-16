package com.alc.archivemanager.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/upload")
    public String index(Model model) {
        model.addAttribute("message", "Грузи, родной!");
        return "uploadPage";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes)
            throws IOException {

        if (file.getOriginalFilename() != ""){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uFileName = UUID.randomUUID() + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + uFileName));


            redirectAttributes.addFlashAttribute("message",
                    "Ого, оно даже загрузилось. Теперь на сервере с именем " + uFileName + "!");
        }

        return "redirect:/upload";
    }
}
