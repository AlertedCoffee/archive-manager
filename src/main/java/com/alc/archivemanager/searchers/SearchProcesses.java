package com.alc.archivemanager.searchers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.parsers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class SearchProcesses implements ISearcher{
    public static final String PDF = ".pdf";
    public static final String DOCX = ".docx";
    public static final String ODT = ".odt";
    public static final String TXT = ".txt";
    public static final String PARSED = ".parsed";


    @Override
    public List<SearchResultModel> Search(List<String> fileNames, List<String> texts, List<String> searchParams) {
        return null;
    }

    protected static boolean CheckAttributes(File file, File parsed) throws IOException {
        BasicFileAttributes parsedAttributes = Files.readAttributes(parsed.toPath(), BasicFileAttributes.class);
        BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        boolean test = parsedAttributes.lastModifiedTime().toMillis() > fileAttributes.lastModifiedTime().toMillis();
        return parsedAttributes.lastModifiedTime().toMillis() > fileAttributes.lastModifiedTime().toMillis();
    }

    protected static List<String> getContent(File file)
    {
        List<String> content = new ArrayList<>();


        if (file.isFile()) {
            int dotIndex = file.getPath().lastIndexOf('.');
            String extension = (dotIndex == -1) ? "" : file.getPath().substring(dotIndex);
            try {
                if (!extension.equals(PARSED)) {
                    File parsed = new File(file.getPath().substring(0, dotIndex) + PARSED);

                    if (parsed.exists() && CheckAttributes(file, parsed)) {
                        content.add(parsed.getPath());
                    } else {
                        content.add(file.getPath());
                    }
                }
            }
            catch (Exception e){
                content.add(file.getPath());
                e.printStackTrace();
            }
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
                IParser parser = ParserFactory(file);
                if (parser != null) {
                    texts.add(parser.Parse(file));
                    searchParams.add(searchParam);
                    returnedFiles.add(file);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        searchResults = Search(returnedFiles, texts, searchParams);

        return searchResults;
    }

    protected IParser ParserFactory(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        return switch (extension){
            case PDF -> new ICEPDFHelper();
            case DOCX -> new ApachePOIHelper();
            case ODT -> new ApacheODFHelper();
            case TXT, PARSED -> new TxtHelper();
            default -> null;
        };

    }
}
