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
@RequestMapping("/authors") // {"author","/authors"}
public class AuthorController {

    private AuthorRepository authorRepository;

    @RequestMapping("/") // {"/index","/"}
    private String index(Model model) {
        List<Author> authors = authorRepository.findAll();
        Collections.reverse(authors);
        model.addAttribute("authors", authors );
        return "authors";
    }

    @RequestMapping
    @ResponseBody
    private List<Author> listall(){
        return authorRepository.findAll();
    }
}
