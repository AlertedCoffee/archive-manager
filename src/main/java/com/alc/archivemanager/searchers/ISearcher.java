package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResult;

public interface ISearcher {
    public SearchResult Search(String text, String searchParam);
}
