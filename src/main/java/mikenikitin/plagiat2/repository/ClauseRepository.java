package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Clause;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClauseRepository extends CrudRepository<Clause, Long> {
//public interface ArticleRepository extends CrudRepository<Article, Long> {
//    Article findArticlesById(Long id);
//    Article findArticlesByName(String name);
//    List<Article> findAll();
}
