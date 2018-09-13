package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Clause;
import mikenikitin.plagiat2.model.Wordbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ClauseRepository extends CrudRepository<Clause, Long> {
    Page<Clause> findAllByPartsEquals(Pageable pageable,Integer parts);
    List<Clause> findAllByPartsEquals(Integer parts);
    List<Clause> findClausesByArticle_Id(Long id);
    Page<Clause> findAll(Pageable pageable);
    List<Clause> findAll();
}
