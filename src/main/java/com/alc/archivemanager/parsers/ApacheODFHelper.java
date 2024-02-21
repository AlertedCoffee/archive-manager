package com.alc.archivemanager.parsers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.EditableTextExtractor;

public class ApacheODFHelper implements IParser{
    @Override
    public String Parse(String filePath) throws Exception {

        TextDocument textdoc=(TextDocument)TextDocument.loadDocument(filePath);

        EditableTextExtractor extractorD = EditableTextExtractor.newOdfEditableTextExtractor(textdoc);

        String output = extractorD.getText();

        return output;

    }
}
