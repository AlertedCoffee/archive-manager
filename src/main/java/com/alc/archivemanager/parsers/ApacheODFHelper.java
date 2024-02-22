package com.alc.archivemanager.parsers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.EditableTextExtractor;

public class ApacheODFHelper implements IParser{
    @Override
    public String Parse(String filePath) throws Exception {

        TextDocument textDoc=(TextDocument)TextDocument.loadDocument(filePath);

        EditableTextExtractor extractorD = EditableTextExtractor.newOdfEditableTextExtractor(textDoc);

        String text = extractorD.getText();

        IParser.SaveParsed(text, filePath);

        return text;

    }
}
