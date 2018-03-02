package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Wordbook {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
//    @Column(name="WORDBOOK_ID")
    private Long id;

    //    Long wc;
    @Order
    private String word;

    //    @Order
    @JsonIgnore
    private Long ugly;

    @Order
    @JsonIgnore
    public String getRevWord() {return new StringBuilder(this.word).reverse().toString();}

//    @OneToMany // (mappedBy="article")
//    @JoinColumn(name="WORD_ID", referencedColumnName="WORDBOOK_ID") // nullable = false
//    private List<Text> text;

//    @OneToMany // (mappedBy = "wordbook")
//    private List<Text> texts;


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

//    public String getWord() {
//        return word;
//    }

}
