package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jdk.nashorn.internal.objects.NativeArray.reverse;

@Controller
@AllArgsConstructor
@RequestMapping({"article","articles"})
public class ArticleController {

    static private boolean reverse=true;
//    @Autowired
    private ArticleRepository articleRepository;

//    protected List<Article> articles = articleRepository.findAll();

    @RequestMapping("/")
    private String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        if (reverse=!reverse) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    @RequestMapping("/name")
    private String orderByName(Model model) {
        List<Article> articles = articleRepository.findAll();
        Collections.sort(articles,(b,a)->(a.getName().compareTo(b.getName())));
        if (reverse=!reverse) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    @RequestMapping("/wc")
    private String orderByWC(Model model) {
        List<Article> articles = articleRepository.findAll();
        Collections.sort(articles,(b,a)->(a.getWc().intValue()-b.getWc().intValue()));
        if (reverse=!reverse) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    @RequestMapping("/title")
    private String orderByTitle(Model model) {
        List<Article> articles = articleRepository.findAll();
        Collections.sort(articles,(b,a)->(a.getTitle().compareTo(b.getTitle())));
        if (reverse=!reverse) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    @RequestMapping("/author")
    private String orderByAuthor(Model model) {
        List<Article> articles = articleRepository.findAll();
        Collections.sort(articles,(b,a)->(a.getAuthor().getRealname().compareTo(b.getAuthor().getRealname())));
        if (reverse=!reverse) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    @RequestMapping
    @ResponseBody
    private List<Article> listall(){return articleRepository.findAll();}

    //    @RequestMapping("{id}")
    @ResponseBody
    private List<String> article(@PathVariable Long id, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<String> list = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) list.add(t.getWord().getWord());
        // System.out.print(t.getWord().getWord());
        // .forEach((b) -> System.out.println(b))
//        articleRepository.findArticlesById(id).getText().forEach((t)->System.out.print(t.getWord().getWord()+" "));
//        articleRepository.findArticlesById(id).getText().map((t)->(t.getWord().getWord()));
//        articleRepository.findArticlesById(id).getText().forEach((t)->(t.getWord().getWord()));
        return list;
    }

    @RequestMapping("{id}")
    private String article(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<String> article = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) article.add(t.getWord().getWord());
        model.addAttribute("article", article);
        return "article";
    }

}
