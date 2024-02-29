package com.alc.archivemanager.parsers;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;

public class ApachePOIHelper implements IParser {
    @Override
    public String parse(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        XWPFDocument document = new XWPFDocument(fis);

        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        fis.close();

        IParser.saveParsed(text, filePath);

        return text;
    }
}
