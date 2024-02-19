package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;

import java.util.List;

public interface ISearcher {
    public List<SearchResultModel> PDFSearchProcess(String mainPath, String searchParam);
    public List<SearchResultModel> Search(List<String> fileName, List<String> text, List<String> searchParam);
}
