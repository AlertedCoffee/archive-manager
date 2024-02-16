package com.alc.archivemanager.PDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ApachePDFBoxHelper implements PDFParser {

    PDFTextStripper textStripper;
    public ApachePDFBoxHelper() throws IOException {
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
