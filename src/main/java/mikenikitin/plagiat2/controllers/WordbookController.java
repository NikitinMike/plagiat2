package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;

@Controller
@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    WordbookRepository wordbookRepository;

    @RequestMapping("/rest")
    @ResponseBody
    private List<Wordbook> listall(){
        return wordbookRepository.findAll();
    }

    @RequestMapping({"/index", "/"})
    private String index(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
//        Collections.
        reverse(wb);
        model.addAttribute("wordbook", wb);
        return "WordBook";
    }

    @RequestMapping("/order")
    private String order(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
        wb.sort((a,b)->(a.getWord().compareTo(b.getWord())));
//        Collections.sort(wb,(a,b)->(a.getWord().compareTo(b.getWord())));
        model.addAttribute("wordbook", wb);
        return "WordBook";
    }

    @RequestMapping("/{sym}")
    private String symbol(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
        //Collections //sort(wb,(a,b)->(a.getWord().compareTo(b.getWord())));
        model.addAttribute("wordbook", wb);
        return "WordBook";
    }

}
