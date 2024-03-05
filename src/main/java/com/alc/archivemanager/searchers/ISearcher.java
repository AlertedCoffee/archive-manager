package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResult;

import java.util.List;

public interface ISearcher {
    public List<SearchResult> searchProcess(String mainPath, String searchParam);
}
