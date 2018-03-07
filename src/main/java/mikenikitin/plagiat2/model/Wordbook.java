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

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="AUTHOR_ID")
    private SpeechPart spart;

    @Order
    @JsonIgnore
    private int size;

    private boolean endtype; // Male/Female

//    @Order
//    @JsonIgnore
//    public String getRevWord() {return new StringBuilder(this.word).reverse().toString();}

    @Order
    @JsonIgnore
    public int getSize() {return size;}

    public String getLetters() {
        return getLetters(true,true);
    }

    public String getLetters(boolean type) {
        return getLetters(type,false);
    }

    public String getLetters(boolean type,boolean reverse) {
        return (type)?getWord(reverse).replaceAll("[аеёиоуыэюя]",""):
            getWord(reverse).replaceAll("[бвгджзйклмнпрстфхцчшщъь]","");
    }

//    @OneToMany // (mappedBy="article")
//    @JoinColumn(name="WORD_ID", referencedColumnName="WORDBOOK_ID") // nullable = false
//    private List<Text> text;

//    @OneToMany // (mappedBy = "wordbook")
//    private List<Text> texts;

    public Wordbook(String s) {
        word = s;
        ugly = 0L;
        size = this.getLetters(false).length();
    }

    public String getWord() {
        return word;
    }

    public String getWord(boolean reverse) {
        return new StringBuilder(getWord().replaceAll("[ьъ]","")).reverse().toString();
//        return (reverse)?new StringBuilder(getWord()).reverse().toString():getWord();
    }

    public void setSize(int size) {
        this.size = size;
    }
}
