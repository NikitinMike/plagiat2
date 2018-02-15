package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.repository.ArticleRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ArticleController {

    ArticleRepository articleRepository;

    @RequestMapping("/{id}")
    private Article hello(@PathVariable Long id){
        return articleRepository.findArticlesById(id);
    }

    @RequestMapping("/")
    private List<Article> listall(){
        return articleRepository.findAll();
    }
}
