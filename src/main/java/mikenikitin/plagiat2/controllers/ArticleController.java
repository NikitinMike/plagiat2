package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
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

    private ArticleRepository articleRepository;
//    protected List<Article> articles = articleRepository.findAll();

    @RequestMapping("/")
    private String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        if (lastOrder.isEmpty()) Collections.reverse(articles);
        model.addAttribute("articles", articles );
        return "indexArticles";
    }

    static private String lastOrder="";
    static private boolean reverse=false;
    @RequestMapping("/order/{order}") // name wc title author
    private String orderBy(@PathVariable String order, Model model) {
        List<Article> articles = articleRepository.findAll();
        switch (order) {
            case "name":
                Collections.sort(articles,(b,a)->(a.getName().compareTo(b.getName())));
                break;
            case "wc":
                Collections.sort(articles,(b,a)->(a.getWc().intValue()-b.getWc().intValue()));
                break;
            case "title":
                Collections.sort(articles,(b,a)->(a.getTitle().compareTo(b.getTitle())));
                break;
            case "author":
                Collections.sort(articles,(b,a)->(a.getAuthor().getRealname().compareTo(b.getAuthor().getRealname())));
                break;
            case "id":
                break;
            default:
                break;
        }
        if (order.equals(lastOrder)) reverse=!reverse;
        if (reverse) Collections.reverse(articles);
        lastOrder=order;
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

    @RequestMapping("/delete/{id}")
//    @ResponseBody
    private String delete(@PathVariable Long id, HttpServletResponse response){
        Article art=articleRepository.findArticlesById(id);
        if(art==null) return "redirect:/articles/order/"+lastOrder;
        articleRepository.delete(art);
        return "redirect:/articles/order/"+lastOrder;
    }

    @RequestMapping("/flat/{id}")
    private String article2(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<String> article = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) article.add(t.getWord().getWord());
        model.addAttribute("article", article);
        return "article";
    }

    @RequestMapping("{id}")
    private String article(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
//        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<Wordbook> article = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) article.add(t.getWord());
        model.addAttribute("wordbook", article);
        return "WordBook";
    }

}
