package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Wordbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface WordbookRepository extends PagingAndSortingRepository<Wordbook,Long> {  // CrudRepository
    List<Wordbook> findAll();

//    Page<Wordbook> listAllByPage(Pageable pageable);
//    List<Wordbook> findAll(Sort sort);
    Page<Wordbook> findAll(Pageable pageable);

//This way the method is returning a List<User>
//    @Query("SELECT t FROM User t")
//    public List<User> findAll(Pageable pageable);

    Wordbook findByWord(String s);
    Wordbook getById(Long id);
    List<Wordbook> findAllByWordLike(String s);

}
