package com.alc.archivemanager.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ApacheIPDFBoxHelper implements IPDFParser {

    PDFTextStripper textStripper;
    public ApacheIPDFBoxHelper() throws IOException {
        textStripper = new PDFTextStripper();
    }

    @Override
    public String Parse(String filePath) {
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            return textStripper.getText(document);
        }
        catch (IOException ex){
            return null;
        }
    }
}
