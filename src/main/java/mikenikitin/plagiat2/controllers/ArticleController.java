package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.repository.ArticleRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping("article")
public class ArticleController {

    ArticleRepository articleRepository;

    @RequestMapping("/{id}")
    private List<String> article(@PathVariable Long id, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<String> list = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) list.add(t.getWord().getWord()); // System.out.print(t.getWord().getWord());
        // .forEach((b) -> System.out.println(b))
//        articleRepository.findArticlesById(id).getText().forEach((t)->System.out.print(t.getWord().getWord()+" "));
//        articleRepository.findArticlesById(id).getText().map((t)->(t.getWord().getWord()));
//        articleRepository.findArticlesById(id).getText().forEach((t)->(t.getWord().getWord()));
        return list;
    }

    @RequestMapping
    private List<Article> listall(){
        return articleRepository.findAll();
    }
}
