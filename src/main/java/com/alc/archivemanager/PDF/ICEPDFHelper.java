package com.alc.archivemanager.PDF;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

public class ICEPDFHelper implements PDFParser{

    @Override
    public String Parse(String filePath) {
        try {

            Document document = new Document();
            document.setFile(filePath);

            StringBuilder text = new StringBuilder();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                String pText = document.getPageText(i).toString();
                text.append(pText);
            }
            return text.toString();
        }
        catch (Exception ex){
            return null;
        }
    }
}
