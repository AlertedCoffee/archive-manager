package com.alc.archivemanager.parsers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.EditableTextExtractor;

public class ApacheODFHelper implements IParser{
    @Override
    public String parse(String filePath) throws Exception {

        TextDocument textDoc=(TextDocument)TextDocument.loadDocument(filePath);

        EditableTextExtractor extractor = EditableTextExtractor.newOdfEditableTextExtractor(textDoc);

        String text = extractor.getText();

        IParser.saveParsed(text, filePath);

        return text;

    }
}
