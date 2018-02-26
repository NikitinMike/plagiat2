package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Wordbook {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
//    @JsonIgnore
    @Column(name="WORDBOOK_ID")
    private Long id;

//    @OneToMany // (mappedBy="article")
//    @JoinColumn(name="WORD_ID", referencedColumnName="WORDBOOK_ID") // nullable = false
//    private List<Text> text;

//    @OneToMany // (mappedBy = "wordbook")
//    private List<Text> texts;

//    Long wc;
    @Order
    private String word;
//    @Order
    private Long ugly;

    public Wordbook (String s){
        word = s;
        ugly=0L;
//        wc=0L;
    }

    public Wordbook (String s,Long wc){
        word = s;
        ugly=0L;
//        this.wc=wc;
    }

    public String getWord() {
        return word;
    }
}
