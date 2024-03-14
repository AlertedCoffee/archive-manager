package com.alc.archivemanager.controllers;

import com.alc.archivemanager.parsers.*;
import com.alc.archivemanager.searchers.ApacheLuceneSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SearchController {
    private final String MESSAGE_ATTRIBUTE = "message";
    private final String SEARCH_PARAM_ATTRIBUTE = "search_param";
    private final String SEARCH_RESULT_ATTRIBUTE = "search_result";



    private final IParser _IParser = new ICEPDFHelper();
    private ISearcher _ISearcher = new ApacheLuceneSearcher();


    public SearchController() throws IOException {
    }

    @GetMapping("/search")
    public String index(@RequestParam(name = "method", required = false) String method,
                        @RequestParam(name = "search_param", required = false) String search_param
//                        Model model,
//                        @ModelAttribute(MESSAGE_ATTRIBUTE) String message
    ){
        return "/searchPage";
    }

    @GetMapping("/test")
    public String test(Model model,
                        @ModelAttribute(MESSAGE_ATTRIBUTE) String message
    ) throws Exception {

        if(message == null || message.isEmpty()) {
            long start = System.currentTimeMillis();

            IParser iParser = new ApacheODFHelper();
            String text = iParser.parse("C:/WebPractice/archive-manager/src/main/resources/storage/Техническое задание.odt");

            model.addAttribute(SEARCH_RESULT_ATTRIBUTE, text);
            long end = System.currentTimeMillis();

            model.addAttribute(MESSAGE_ATTRIBUTE, "Время парсинга: " + (end - start) + "мс");
        }
        else {
            model.addAttribute(MESSAGE_ATTRIBUTE, message);
        }



        return "/testPage";
    }
}
