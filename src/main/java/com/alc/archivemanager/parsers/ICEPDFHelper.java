package com.alc.archivemanager.parsers;

import org.icepdf.core.pobjects.Document;

public class ICEPDFHelper implements IParser {

    @Override
    public String parse(String filePath) throws Exception {
        Document document = new Document();
        document.setFile(filePath);

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            String pText = document.getPageText(i).toString();
            text.append(pText).append("\r\n\r\n");
        }

        IParser.saveParsed(text.toString(), filePath);

        return text.toString();
    }
}
