package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;

import java.util.List;

public interface ISearcher {

    public SearchResultModel Search(String text, String searchParam);
}
