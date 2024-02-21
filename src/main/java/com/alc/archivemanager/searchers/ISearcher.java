package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;

import java.util.List;

public interface ISearcher {
    public List<SearchResultModel> SearchProcess(String mainPath, String searchParam);
    public List<SearchResultModel> Search(List<String> fileNames, List<String> texts, List<String> searchParams);
}
