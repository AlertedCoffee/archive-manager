package com.alc.archivemanager.parsers;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;

import java.io.File;
import java.util.List;

public class Docx4jHelper implements IParser{
    @Override
    public String Parse(String filePath) {

        try {

            WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(filePath));

            // Получите основную часть документа
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();


            StringBuffer df = new StringBuffer();
            // Итерация по параграфам и извлечение текста
            for (Object paragraph : documentPart.getContent()) {
                if (paragraph instanceof org.docx4j.wml.P) {
                    df.append((org.docx4j.wml.P) paragraph).append("\r\n");
                    // Здесь вы можете обработать текст параграфа по вашему усмотрению
                }
            }

            return df.toString();

//            String extractedText = extractTextFromDocx(filePath);
//            return extractedText;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String extractTextFromDocx(String filePath) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(filePath));

        StringBuilder extractedText = new StringBuilder();
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        extractedText.append(extractTextFromMainDocumentPart(mainDocumentPart));

        return extractedText.toString();
    }


    private static String extractTextFromMainDocumentPart(MainDocumentPart mainDocumentPart) {
        StringBuilder extractedText = new StringBuilder();
        List<Object> content = mainDocumentPart.getContent();
        for (Object obj : content) {
            if (obj instanceof org.docx4j.wml.P) {
                org.docx4j.wml.P paragraph = (org.docx4j.wml.P) obj;
                extractedText.append(extractTextFromParagraph(paragraph));
            }
            extractedText.append(content.toString());
        }
        return extractedText.toString();
    }

    private static String extractTextFromParagraph(P paragraph) {
        StringBuilder extractedText = new StringBuilder();
        List<Object> content = paragraph.getContent();
        for (Object obj : content) {
            if (obj instanceof Text) {
                Text textElement = (Text) obj;
                extractedText.append(textElement.getValue());
                extractedText.append(" "); // Add space between text elements
            }
        }
        return extractedText.toString();
    }
}
