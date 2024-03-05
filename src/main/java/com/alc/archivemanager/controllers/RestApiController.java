package com.alc.archivemanager.controllers;

import com.alc.archivemanager.model.FileSystemItem;
import com.alc.archivemanager.model.FileType;
import com.alc.archivemanager.model.SearchResult;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.ComboSearcher;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import com.alc.archivemanager.util.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam(name = "method", required = false) String method,
                                     @RequestParam(name = "search_param", required = false) String search_param) throws Exception {

        if(search_param == null || search_param.isEmpty()) throw new Exception("Параметр поиска не задан");

        ISearcher searcher = switch (method) {
            case "neural" -> new DeepPavlovSearcher();
            case "combo" -> new ComboSearcher();
            default -> new ApacheLuceneSearcher();
        };

        List<SearchResult> searchResult = searcher.searchProcess("C:/WebPractice/archive-manager/src/main/resources/storage/", search_param);
        searchResult.sort(Comparator.comparingDouble(SearchResult::getCoincidence).reversed());

        return searchResult;
    }

    @GetMapping("/get_files")
    public List<FileSystemItem> getFiles(
            @RequestParam(name = "main_path", required = false) String mainPath
    ) {
        List<FileSystemItem> xui = FileUtil.getFiles(mainPath == null || mainPath.isEmpty() ? "C:/WebPractice/archive-manager/src/main/resources/storage/" : mainPath);
        if(xui.isEmpty()){
            xui.add(new FileSystemItem(mainPath, FileType.SHADOW, mainPath));
        }
        return xui;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("destination") String destination) {
        // Проверяем, был ли передан файл
        if (file.isEmpty()) {
            return new ResponseEntity<>("Пожалуйста, выберите файл для загрузки.", HttpStatus.BAD_REQUEST);
        }

        // Проверяем, было ли передано место для сохранения файла
        if (destination.isEmpty() || destination.equals("null")) {
            destination = "C:/WebPractice/archive-manager/src/main/resources/storage/";
        }

        try {
            File uploadDir = new File(destination);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }



            String uFileName = UUID.randomUUID() + "." + file.getOriginalFilename();
            File uploadedFile = new File(destination + uFileName);
            file.transferTo(uploadedFile);

            return new ResponseEntity<>("Файл успешно загружен по пути: " + uploadedFile.getAbsolutePath(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Ошибка при загрузке файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
