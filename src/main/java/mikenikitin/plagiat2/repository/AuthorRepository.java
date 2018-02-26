package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository <Author,Long> {
    List<Author> findAll();
    Author findByName(String name);
    Author findAuthorsById(Long id);
}
