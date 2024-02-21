package com.alc.archivemanager.parsers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.EditableTextExtractor;

public class ApacheODFHelper implements IParser{
    @Override
    public String Parse(String filePath) {
        try {
            // Открываем документ
            TextDocument textdoc=(TextDocument)TextDocument.loadDocument(filePath);

            EditableTextExtractor extractorD = EditableTextExtractor.newOdfEditableTextExtractor(textdoc);

            String output = extractorD.getText();

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
