package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Author;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.AuthorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/authors") //  {"author","/authors"}
public class AuthorController {

    private ArticleRepository articleRepository;
    private AuthorRepository authorRepository;
//    static private boolean reverse;

    @RequestMapping("/") // {"/index","/"}
    private String index(Model model) {
        List<Author> authors = authorRepository.findAll();
//        if (reverse=!reverse)
        Collections.reverse(authors);
        model.addAttribute("authors", authors );
        return "authors";
    }

    static private boolean reverse=false;
    static private String lastOrder="";
    @RequestMapping("/order/{order}") // name wc title author
    private String orderBy(@PathVariable String order, Model model) {
        List<Author> authors = authorRepository.findAll();
        switch (order) {
            case "count":
                Collections.sort(authors,(b,a)->(a.getArticles().size()-b.getArticles().size()));
                break;
            case "name":
                Collections.sort(authors,(a,b)->(a.getName().compareTo(b.getName())));
                break;
            case "realname":
                Collections.sort(authors,(a,b)->(a.getRealname().compareTo(b.getRealname())));
                break;
            case "id":
                break;
            default:
                break;
        }
        if (order.equals(lastOrder)) reverse=!reverse;
        if (reverse) Collections.reverse(authors);
        lastOrder=order;
        model.addAttribute("authors", authors );
        return "authors";
    }

    @RequestMapping("{id}")
//    @ResponseBody
    private String articles(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        if(authorRepository.findAuthorsById(id)==null){response.sendRedirect("/authors");return null;}
//        System.out.println(authorRepository.findAuthorsById(id));
        List<Article> articles = new ArrayList<>(); // = null;
        for (Article a:authorRepository.findAuthorsById(id).getArticles()) articles.add(a);
        model.addAttribute("author", authorRepository.findAuthorsById(id).getRealname());
        model.addAttribute("articles", articles);
        return "indexArticles";
//        return articles;
    }

    @RequestMapping("/delete/{id}")
//    @ResponseBody
    private String delete(@PathVariable Long id, HttpServletResponse response){
        Author a=authorRepository.findAuthorsById(id);
        if(a==null) return "redirect:/authors/";
        List<Article> articles = new ArrayList<>(); // = null;
        for (Article art:authorRepository.findAuthorsById(id).getArticles()) articleRepository.delete(art);
        authorRepository.delete(a);
        return "redirect:/authors/order/"+lastOrder;
    }
    @RequestMapping
    @ResponseBody
    private List<Author> listall(){
        return authorRepository.findAll();
    }
}
