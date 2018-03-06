package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;

@Controller
@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    private WordbookRepository wordbookRepository;
    private TextRepository textRepository;
    private ArticleRepository articleRepository;

    @RequestMapping("/rest")
    @ResponseBody
    private List<Wordbook> listall(){
        return wordbookRepository.findAll();
    }

    @RequestMapping({"/index", "/"})
    private String index(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
        Collections.sort(wb,(a, b)->(a.getWord(true).compareTo(b.getWord(true))));
//        Collections.sort(wb,(a, b)->(a.getLetters().compareTo(b.getLetters())));
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

    @RequestMapping("/word/{id}")
//    @ResponseBody
    private String id(@PathVariable Long id,Model model) {
        List<Article> articles=new ArrayList<>();
        for (Text txt:textRepository.getAllByWord(wordbookRepository.getById(id)))
//            System.out.println(txt.getArticle().getTitle());
            if (!articles.contains(txt.getArticle()))
                if (articleRepository.existsById(txt.getArticle().getId()))
                    articles.add(txt.getArticle());
        model.addAttribute("articles", articles);
        return "indexArticles";
//        return texts.stream().map(text -> text.getArticle()).collect(Collectors.toList());
    }

    @RequestMapping("/{sym}")
    private String symbol(Model model,@PathVariable String sym) {
        List<Wordbook> wb=wordbookRepository.findAllByWordLike(sym+'%');
        model.addAttribute("wordbook", wb);
        return "WordBook";
    }

    @RequestMapping("/fill")
    @ResponseBody
    private void fillWB(){
        List<Wordbook> wb=wordbookRepository.findAll();
        wb.forEach((a)->a.setSize(a.getLetters(false).length()));

//        wb.forEach(System.out::println);
//        wb.forEach(Wordbook::getWord);

//        List<String> a = Arrays.asList("123","234","345");
//        List<Integer> b = a.stream()
//                .map(String::length)
//                .collect(Collectors.toList());

        wordbookRepository.saveAll(wb);
    }

}
