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
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @JsonIgnore
    Long id;

    Long article_id;
    Long word_id;
    Long position;

    public Text(Long article_id,Long word_id,Long position){
        this.article_id=article_id;
        this.word_id=word_id;
        this.position=position;
    }

}
