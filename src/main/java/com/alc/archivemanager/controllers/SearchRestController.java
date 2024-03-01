package com.alc.archivemanager.controllers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.ComboSearcher;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
public class SearchRestController {

    @GetMapping("/api/search")
    public List<SearchResultModel> search(@RequestParam(name = "method", required = false) String method,
                                          @RequestParam(name = "search_param", required = false) String search_param) throws Exception {

        if(search_param == null || search_param.isEmpty()) throw new Exception("Параметр поиска не задан");

        ISearcher searcher = switch (method) {
            case "neural" -> new DeepPavlovSearcher();
            case "combo" -> new ComboSearcher();
            default -> new ApacheLuceneSearcher();
        };

        List<SearchResultModel> searchResult = searcher.searchProcess("C:/WebPractice/archive-manager/src/main/resources/storage/", search_param);
        searchResult.sort(Comparator.comparingDouble(SearchResultModel::getCoincidence).reversed());

        return searchResult;
    }
}
