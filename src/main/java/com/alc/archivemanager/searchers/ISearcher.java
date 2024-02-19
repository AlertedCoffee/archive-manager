package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;

import java.util.List;

public interface ISearcher {
    public List<SearchResultModel> PDFSearchProcess(String mainPath, String searchParam);
    public SearchResultModel Search(String fileName, String text, String searchParam);
}
