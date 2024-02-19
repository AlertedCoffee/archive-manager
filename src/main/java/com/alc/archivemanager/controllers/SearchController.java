package com.alc.archivemanager.controllers;

import com.alc.archivemanager.model.SearchResultModel;
import com.alc.archivemanager.pdf.*;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.directory.SearchResult;
import java.io.IOException;

@Controller
public class SearchController {
    private final String MESSAGE_ATTRIBUTE = "message";
    private final String SEARCH_PARAM_ATTRIBUTE = "search_param";
    private final String SEARCH_RESULT_ATTRIBUTE = "search_result";



    private final IPDFParser _IPDFParser = new ICEPDFHelper();
    private final ISearcher _ISearcher = new DeepPavlovSearcher();


    public SearchController() throws IOException {
    }

    @GetMapping("/search")
    public String index(@RequestParam(name = "search_param", required = false) String search_param,
                        Model model,
                        @ModelAttribute(MESSAGE_ATTRIBUTE) String message
    ){

        if(message != null && !message.isEmpty()) {
            model.addAttribute(MESSAGE_ATTRIBUTE, message);
        }

        if(search_param != null && !search_param.isEmpty()){
            model.addAttribute(SEARCH_PARAM_ATTRIBUTE, search_param);

            String parsedText = _IPDFParser.Parse("C:/WebPractice/archive-manager/src/main/resources/storage/bf1b1ad8-ac8d-467e-901d-19a7586d21b6.19.201-78 Техническое задание. Требования к содержанию и оформлению.pdf");
            SearchResultModel searched = _ISearcher.Search(parsedText, search_param);
            model.addAttribute(SEARCH_RESULT_ATTRIBUTE, searched.Answer);

            model.addAttribute(MESSAGE_ATTRIBUTE, parsedText);
        }

        return "/searchPage";
    }
}
