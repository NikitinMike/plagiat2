package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonIgnore
    Long id;
    String name;
    Long wc;
//    List<String> words;

    public Article(String name, Long wc) {
        this.name = name;
        this.wc=wc;
    }

    public Article(String name) {
        this.name = name;
        wc=0L;
    }

//    public void setwords(List<String> words){
//        this.words=words;
//    }

}
