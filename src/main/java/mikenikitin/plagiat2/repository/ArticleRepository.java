package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long>{
    Article findArticlesById(Long id);
    List<Article> findArticlesByName(String name);
    List<Article> findArticleByAuthor(Author author);
    Page<Article> findAll(Pageable pageable);
    List<Article> findAll();
}
