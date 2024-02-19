package com.alc.archivemanager.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ApacheIPDFBoxHelper implements IPDFParser {

    PDFTextStripper textStripper;
    public ApacheIPDFBoxHelper() {
        try {
            textStripper = new PDFTextStripper();
        }
        catch (Exception e){}
    }

    @Override
    public String Parse(String filePath) {
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            String text = textStripper.getText(document);
            document.close();
            return text;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
