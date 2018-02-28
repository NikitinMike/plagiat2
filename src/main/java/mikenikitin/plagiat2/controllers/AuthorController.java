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

    private AuthorRepository authorRepository;

    @RequestMapping("/") // {"/index","/"}
    private String index(Model model) {
        List<Author> authors = authorRepository.findAll();
        Collections.reverse(authors);
        model.addAttribute("authors", authors );
        return "authors";
    }

    @RequestMapping("/count") // {"/index","/"}
    private String orderByCount(Model model) {
        List<Author> authors = authorRepository.findAll();
        Collections.sort(authors,(b,a)->(a.getArticles().size()-b.getArticles().size()));
        model.addAttribute("authors", authors );
        return "authors";
    }

    @RequestMapping("/name") // {"/index","/"}
    private String orderByName(Model model) {
        List<Author> authors = authorRepository.findAll();
        Collections.sort(authors,(a,b)->(a.getName().compareTo(b.getName())));
        model.addAttribute("authors", authors );
        return "authors";
    }

    @RequestMapping("/realname") // {"/index","/"}
    private String orderByRealName(Model model) {
        List<Author> authors = authorRepository.findAll();
        Collections.sort(authors,(a,b)->(a.getRealname().compareTo(b.getRealname())));
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
        return "articles";
//        return articles;
    }

    @RequestMapping
    @ResponseBody
    private List<Author> listall(){
        return authorRepository.findAll();
    }
}
