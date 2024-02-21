package com.alc.archivemanager.parsers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class ApacheIBoxHelper implements IParser {

    PDFTextStripper textStripper;
    public ApacheIBoxHelper() {
        try {
            textStripper = new PDFTextStripper();
        }
        catch (Exception e){}
    }

    @Override
    public String Parse(String filePath) throws Exception{
        PDDocument document = PDDocument.load(new File(filePath));
        String text = textStripper.getText(document);
        document.close();
        return text;
    }
}
