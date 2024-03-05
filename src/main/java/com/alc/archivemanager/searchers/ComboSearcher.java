package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class ComboSearcher implements ISearcher{
    @Override
    public List<SearchResult> searchProcess(String mainPath, String searchParam) {
        ApacheLuceneSearcher apache = new ApacheLuceneSearcher();
        List<SearchResult> apacheResult = apache.searchProcess(mainPath, searchParam);


        double max = apacheResult.stream()
                .mapToDouble(SearchResult::getCoincidence)
                .max()
                .orElse(Double.MIN_VALUE);
        if (max > 0.7){
            apacheResult.removeIf(result -> result.Coincidence < 0.7);
        }


        DeepPavlovSearcher pavlov = new DeepPavlovSearcher();

        List<String> files = new ArrayList<>();
        for(SearchResult result : apacheResult) files.add(result.FileName);

        List<SearchResult> pavlovResult = pavlov.searchProcess(files, searchParam);

//        List<SearchResultModel> result = new ArrayList<>();
//
//        for(SearchResultModel item : pavlovResult){
//            List<String> oneList = new ArrayList<>();
//            oneList.add(item.FileName);
//            result.addAll(apache.searchProcess(oneList, item.Answer));
//        }

        return pavlovResult;
    }
}
