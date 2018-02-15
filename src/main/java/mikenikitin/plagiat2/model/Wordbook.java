package mikenikitin.plagiat2.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Wordbook {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
//    @JsonIgnore
    Long id;

//    Long wc;
    String word;

    public Wordbook (String s){
        word = s;
//        wc=0L;
    }

    public Wordbook (String s,Long wc){
        word = s;
//        this.wc=wc;
    }

}