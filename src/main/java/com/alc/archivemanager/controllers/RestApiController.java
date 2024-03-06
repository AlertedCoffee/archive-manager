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

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final String MAIN_PATH = "C:/WebPractice/archive-manager/src/main/resources/";
    private final String STORAGE_SUFFIX = "storage/";

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam(name = "method", required = false) String method,
                                     @RequestParam(name = "search_param", required = false) String search_param) throws Exception {

        if(search_param == null || search_param.isEmpty()) throw new Exception("Параметр поиска не задан");

        ISearcher searcher = switch (method) {
            case "neural" -> new DeepPavlovSearcher();
            case "combo" -> new ComboSearcher();
            default -> new ApacheLuceneSearcher();
        };

        List<SearchResult> searchResult = searcher.searchProcess(MAIN_PATH + STORAGE_SUFFIX, search_param);
        searchResult.sort(Comparator.comparingDouble(SearchResult::getCoincidence).reversed());

        return searchResult;
    }

    @GetMapping("/get_files")
    public ResponseEntity<List<FileSystemItem>> getFiles(
            @RequestParam(name = "path", required = false) String path
    ) {
        if (path.contains("..")) return new ResponseEntity<>(HttpStatus.LOCKED);
        List<FileSystemItem> files = FileUtil.getFiles(path == null || path.isEmpty() ? MAIN_PATH + STORAGE_SUFFIX : MAIN_PATH + path);
        if(files.isEmpty()){
            files.add(new FileSystemItem(path, FileType.SHADOW, path));
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("destination") String destination) {
        // Проверяем, был ли передан файл
        if (file.isEmpty()) {
            return new ResponseEntity<>("Пожалуйста, выберите файл для загрузки.", HttpStatus.BAD_REQUEST);
        }

        if (destination.contains("..")) return new ResponseEntity<>("Отказано в доступе", HttpStatus.LOCKED);

        // Проверяем, было ли передано место для сохранения файла
        if (destination.isEmpty() || destination.equals("null")) {
            destination = MAIN_PATH + STORAGE_SUFFIX;
        }
        else destination  = MAIN_PATH + destination;

        try {
            FileUtil.saveFile(file, destination);

            return new ResponseEntity<>("Файл успешно загружен.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Ошибка при загрузке файла: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
