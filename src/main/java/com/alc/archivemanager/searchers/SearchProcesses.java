package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.ApacheIBoxHelper;
import com.alc.archivemanager.parsers.IParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class SearchProcesses implements ISearcher{

    private final IParser _IParser = new ApacheIBoxHelper();
    private static final String PDF = ".pdf";

    @Override
    public List<SearchResultModel> Search(List<String> fileNames, List<String> texts, List<String> searchParams) {
        return null;
    }

    private static List<String> getContent(File file)
    {
        List<String> content = new ArrayList<>();


        if (file.isFile()) {
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf('.');
            String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
            if (extension.equalsIgnoreCase(PDF))
                content.add(file.getPath());
        }
        else {
            File[] SubDirectory = file.listFiles();
            for (File SubWay:SubDirectory)
                content.addAll(getContent(SubWay));
        }

        return content;
    }

    @Override
    public List<SearchResultModel> SearchProcess(String mainPath, String searchParam){
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<String> files = new ArrayList<>(getContent(dir));

        List<SearchResultModel> searchResults = new ArrayList<>();

        List<String> texts = new ArrayList<>();
        List<String> searchParams = new ArrayList<>();
        for (String file : files){
            texts.add(_IParser.Parse(file));
            searchParams.add(searchParam);
        }



        searchResults = Search(files, texts, searchParams);


        return searchResults;
    }
}
