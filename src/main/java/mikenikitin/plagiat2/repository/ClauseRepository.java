package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Clause;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClauseRepository extends CrudRepository<Clause, Long> {
    List<Clause> findAll();
    List<Clause> findClausesByArticle_Id(Long id);
}
