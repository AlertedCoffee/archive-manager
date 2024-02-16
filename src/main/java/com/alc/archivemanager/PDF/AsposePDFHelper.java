package com.alc.archivemanager.PDF;

import com.aspose.pdf.Document;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.TextExtractionOptions;

public class AsposePDFHelper implements PDFParser{
    @Override
    public String Parse(String filePath) {
        Document pdfDocument = new Document(filePath);


        TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

        pdfDocument.getPages().accept(textAbsorber);

        String extractedText = textAbsorber.getText();

        return extractedText;

    }
}
