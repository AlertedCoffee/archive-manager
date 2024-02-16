//package com.alc.archivemanager.PDF;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//import org.pdfclown.PDF;
//import org.pdfclown.documents.Document;
//import org.pdfclown.documents.Page;
//import org.pdfclown.documents.contents.ContentScanner;
//import org.pdfclown.documents.contents.fonts.Font;
//import org.pdfclown.documents.contents.objects.ContainerObject;
//import org.pdfclown.documents.contents.objects.ContentObject;
//import org.pdfclown.documents.contents.objects.ShowText;
//import org.pdfclown.documents.contents.objects.Text;
//
//public class PDFClownHelper implements PDFParser {
//
//    public PDFClownHelper() {
//    }
//
//    public boolean run(
//    )
//    {
//        String filePath = "C:/WebPractice/archive-manager/src/main/resources/storage/19.106-78  Требования к программным документам, выполненным печатным способом.pdf";
//
//        // 1. Open the PDF file!
//        org.pdfclown.files.File file;
//        try
//        {file = new org.pdfclown.files.File(filePath);}
//        catch(Exception e)
//        {throw new RuntimeException();}
//
//        // 2. Get the PDF document!
//        Document document = file.getDocument();
//
//        // 3. Extracting text from the document pages...
//        for(Page page : document.getPages())
//        {
//            if(!prompt(page))
//                return false;
//
//            extract(
//                    new ContentScanner(page) // Wraps the page contents into a scanner.
//            );
//        }
//
//        return true;
//    }
//
//    /**
//     Scans a content level looking for text.
//     */
//  /*
//    NOTE: Page contents are represented by a sequence of content objects,
//    possibly nested into multiple levels.
//  */
//    private void extract(
//            ContentScanner level
//    )
//    {
//        if(level == null)
//            return;
//
//        while(level.moveNext())
//        {
//            ContentObject content = level.getCurrent();
//            if(content instanceof ShowText)
//            {
//                Font font = level.getState().getFont();
//                // Extract the current text chunk, decoding it!
//                System.out.println(font.decode(((ShowText)content).getText()));
//            }
//            else if(content instanceof Text
//                    || content instanceof ContainerObject)
//            {
//                // Scan the inner level!
//                extract(level.getChildLevel());
//            }
//        }
//    }
//
//    private boolean prompt(
//            Page page
//    )
//    {
//        int pageIndex = page.getIndex();
////        if(pageIndex ? 0)
////        {
////            Map&lt;String,String&gt; options = new HashMap&lt;String,String&gt;();
////            options.put(&quot;&quot;, &quot;Scan next page&quot;);
////            options.put(&quot;Q&quot;, &quot;End scanning&quot;);
////            if(!promptChoice(options).equals(&quot;&quot;))
////            return false;
////        }
//
//        return true;
//    }
//
//
//    @Override
//    public String Parse(String filePath) {
//        run();
//        return null;
//    }
//}
