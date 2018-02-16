package mikenikitin.plagiat2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Wordbook {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    @Column(name="WORDBOOK_ID")
    private long id;

//    @OneToMany // (mappedBy="article")
//    @JoinColumn(name="WORD_ID", referencedColumnName="WORDBOOK_ID") // nullable = false
//    private List<Text> text;

//    @OneToMany // (mappedBy = "wordbook")
//    private List<Text> texts;

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
