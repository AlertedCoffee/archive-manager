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

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class RestApiController {

    @GetMapping("/api/search")
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

    @GetMapping("/api/get_files")
    public List<FileSystemItem> getFiles(
            @RequestParam(name = "main_path", required = false) String mainPath
    ) {
        List<FileSystemItem> xui = FileUtil.getFiles(mainPath == null || mainPath.isEmpty() ? "C:/WebPractice/archive-manager/src/main/resources/storage/" : mainPath);
        String test = xui.get(0).getName();

        return xui;
    }
}
