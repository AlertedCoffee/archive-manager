package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.pdf.ApacheIPDFBoxHelper;
import com.alc.archivemanager.pdf.ICEPDFHelper;
import com.alc.archivemanager.pdf.IPDFParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchProcesses implements ISearcher{

    private final IPDFParser _IPDFParser = new ApacheIPDFBoxHelper();
    private static final String PDF = ".pdf";

    @Override
    public SearchResultModel Search(String text, String searchParam) {
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

    public List<SearchResultModel> PDFSearchProcess(String mainPath, String searchParam){
        File dir = new File(mainPath);

        if(!dir.exists())
            return null;

        List<String> files = new ArrayList<>(getContent(dir));

        List<SearchResultModel> searchResults = new ArrayList<>();
        for (String file : files){
            String text = _IPDFParser.Parse(file);
            searchResults.add(Search(text, searchParam));
        }


        return searchResults;
    }
}
