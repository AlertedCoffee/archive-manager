package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.ApacheIBoxHelper;
import com.alc.archivemanager.parsers.ApacheODFHelper;
import com.alc.archivemanager.parsers.ApachePOIHelper;
import com.alc.archivemanager.parsers.IParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class SearchProcesses implements ISearcher{

    private final IParser _IParser = new ApacheIBoxHelper();
    private static final String PDF = ".pdf";
    private static final String DOCX = ".docx";
    private static final String ODT = ".odt";

    @Override
    public List<SearchResultModel> Search(List<String> fileNames, List<String> texts, List<String> searchParams) {
        return null;
    }

    private static List<String> getContent(File file)
    {
        List<String> content = new ArrayList<>();


        if (file.isFile()) {
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

        List<SearchResultModel> searchResults;

        List<String> texts = new ArrayList<>();
        List<String> searchParams = new ArrayList<>();
        List<String> returnedFiles = new ArrayList<>();

        for (String file : files){
            try {
                long start = System.currentTimeMillis();
                IParser parser = ParserFactory(file);
                if (parser != null) {
                    texts.add(parser.Parse(file));
                    searchParams.add(searchParam);
                    returnedFiles.add(file);
                }
                long end = System.currentTimeMillis();
                System.out.println(end - start);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        searchResults = Search(returnedFiles, texts, searchParams);

        return searchResults;
    }

    private IParser ParserFactory(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        return switch (extension){
            case PDF -> new ApacheIBoxHelper();
            case DOCX -> new ApachePOIHelper();
            case ODT -> new ApacheODFHelper();
            default -> null;
        };

    }
}
