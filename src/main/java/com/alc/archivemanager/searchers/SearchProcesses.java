package com.alc.archivemanager.searchers;

import com.alc.archivemanager.parsers.*;
import com.alc.archivemanager.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public abstract class SearchProcesses implements ISearcher{
    public static final String PDF = "pdf";
    public static final String DOCX = "docx";
    public static final String ODT = "odt";
    public static final String TXT = "txt";
    public static final String PARSED = "parsed";


    protected static boolean checkAttributes(File file, File parsed) throws IOException {
        BasicFileAttributes parsedAttributes = Files.readAttributes(parsed.toPath(), BasicFileAttributes.class);
        BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return parsedAttributes.lastModifiedTime().toMillis() > fileAttributes.lastModifiedTime().toMillis();
    }

    protected static List<String> getContent(File file)
    {
        List<String> content = new ArrayList<>();


        if (file.isFile()) {
            int dotIndex = file.getPath().lastIndexOf('.');
            String extension = com.google.common.io.Files.getFileExtension(file.getName());
            try {
                if (!extension.equals(PARSED)) {
                    File parsed = FileUtil.getParsed(file);

                    if (parsed.exists() && checkAttributes(file, parsed)) {
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

    protected IParser parserFactory(String fileName){
        String extension = com.google.common.io.Files.getFileExtension(fileName);

        return switch (extension){
            case PDF -> new ICEPDFHelper();
            case DOCX -> new ApachePOIHelper();
            case ODT -> new ApacheODFHelper();
            case TXT, PARSED -> new TxtHelper();
            default -> null;
        };

    }
}
