package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TextRepository extends CrudRepository <Text,Long> {
    List<Text> findAll();

    List<Text> getAllByWord(Wordbook wb);

//    List<Text> findAllByWord(Wordbook wb);
//    Text findByWord(String s);
}
