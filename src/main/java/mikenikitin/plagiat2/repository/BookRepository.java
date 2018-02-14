package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findBooksByIssue(Integer issue);
}
