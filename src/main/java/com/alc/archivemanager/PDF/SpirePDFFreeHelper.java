package com.alc.archivemanager.PDF;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.exporting.text.SimpleTextExtractionStrategy;
import com.spire.pdf.texts.PdfTextExtractOptions;
import com.spire.pdf.texts.PdfTextExtractor;

import java.io.File;

public class SpirePDFFreeHelper implements PDFParser{
    @Override
    public String Parse(String filePath) {

        PdfDocument doc=new PdfDocument();
        //Загрузите файл PDF
        doc.loadFromFile(filePath);

        //Создать экземпляр StringBuilder
        StringBuilder sb=new StringBuilder();

        PdfPageBase page;

        //Перебирайте страницы PDF и получайте текст каждой страницы
        for(int i=0;i<3;i++){
            page=doc.getPages().get(i);
            PdfTextExtractor textExtractor = new PdfTextExtractor(page);

            PdfTextExtractOptions extractOptions = new PdfTextExtractOptions();
            extractOptions.setSimpleExtraction(true);

            sb.append(textExtractor.extract(extractOptions));
//            sb.append(page.extractText(true));
        }


        doc.close();
        return sb.toString();
    }
}
