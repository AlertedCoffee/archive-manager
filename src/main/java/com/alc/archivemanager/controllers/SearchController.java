package com.alc.archivemanager.controllers;

import com.alc.archivemanager.pdf.*;
import com.alc.archivemanager.searchers.DeepPavlovSearcher;
import com.alc.archivemanager.searchers.ISearcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

            String parsedText = _IPDFParser.Parse("C:/WebPractice/archive-manager/src/main/resources/storage/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.19.106-78  Требования к программным документам, выполненным печатным способом.pdf");
            model.addAttribute(SEARCH_RESULT_ATTRIBUTE, _ISearcher.Search(parsedText, search_param).Answer);

            model.addAttribute(MESSAGE_ATTRIBUTE, parsedText);
        }

        return "/searchPage";
    }

    @GetMapping("/testParser")
    public String parser(RedirectAttributes attributes){
        Long startTime = System.currentTimeMillis();

        String text = _IPDFParser.Parse("C:/WebPractice/archive-manager/src/main/resources/storage/f7cb5222-32a2-4ab0-93fe-50b96e8067cc.19.106-78  Требования к программным документам, выполненным печатным способом.pdf");
        Long endTime = System.currentTimeMillis();

        attributes.addFlashAttribute(MESSAGE_ATTRIBUTE, String.format("Время парса: %s мс \n %s", endTime - startTime, text));
        return "redirect:/search";
    }

    @GetMapping("/testParser2")
    public String parser2(RedirectAttributes attributes){
        Long startTime = System.currentTimeMillis();

        String text = _IPDFParser.Parse("C:/WebPractice/archive-manager/src/main/resources/storage/19.106-78  Требования к программным документам, выполненным печатным способом.pdf");
        Long endTime = System.currentTimeMillis();

        attributes.addFlashAttribute(MESSAGE_ATTRIBUTE, String.format("Время парса: %s мс \n %s", endTime - startTime, text));
        return "redirect:/search";
    }
}
