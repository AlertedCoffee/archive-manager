package com.alc.archivemanager.parsers;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;

public class ApachePOIHelper implements IParser {
    @Override
    public String Parse(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis);

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String text = extractor.getText();
            fis.close();

            return text;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
