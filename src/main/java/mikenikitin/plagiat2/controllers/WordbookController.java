package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

@Controller
@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    private WordbookRepository wordbookRepository;
    private TextRepository textRepository;

    @RequestMapping("/rest")
    @ResponseBody
    private List<Wordbook> listall(){
        return wordbookRepository.findAll();
    }

    @RequestMapping({"/index", "/"})
    private String index(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
        Collections.sort(wb,(a, b)->(a.getRevWord().compareTo(b.getRevWord())));
//        reverse(wb);
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

    @RequestMapping("/word/{id}")
    @ResponseBody
    private List<Text> id(@PathVariable Long id) {
        Wordbook wb=wordbookRepository.getById(id);
        System.out.println(wb);
        List<Text> text=textRepository.getAllByWord(wb); // findAllByWord(wordbookRepository.getById(id));
        System.out.println(text);
        return null;
    }

    @RequestMapping("/{sym}")
    private String symbol(Model model,@PathVariable String sym) {
        List<Wordbook> wb=wordbookRepository.findAllByWordLike(sym+'%');
        model.addAttribute("wordbook", wb);
        return "WordBook";
    }

}
