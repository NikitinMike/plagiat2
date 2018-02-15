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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @JsonIgnore
    Long id;

    String word;

    public Wordbook (String s){
        word = s;
    }

}
