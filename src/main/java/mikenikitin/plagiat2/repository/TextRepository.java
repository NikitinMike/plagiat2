package mikenikitin.plagiat2.repository;

import mikenikitin.plagiat2.model.Text;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TextRepository extends CrudRepository <Text,Long> {
    List<Text> findAll();
//    Text findByWord(String s);
}
