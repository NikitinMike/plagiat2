package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Wordbook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordbookRepository extends CrudRepository <Wordbook,Long> {
    List<Wordbook> findAll();
}
