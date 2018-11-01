package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@Entity
public class Wordbook implements Comparable<Wordbook> {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
//    @Column(name="WORDBOOK_ID")
    private Long id;

    //    Long wc;
    @OrderBy
    private String word;

    private int strike;
    private String description;

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

    @JsonIgnore
    private boolean endtype; // Male/Female
    //

    @JsonIgnore
    public String getEnd() {
        return getEnd(false);
    }

//    @Column(unique=true)
    @JsonIgnore
    private String getEnd(boolean reverse) { // ([аеёиоуыэюя] [^аеёиоуыэюя]
//        Matcher m = Pattern.compile("([аеёиоуыэюя]?[^аеёиоуыэюя]*[ъь]??[аеёиоуыэюя]+[^аеёиоуыэюя]*)$")
//        Matcher m = Pattern.compile("([аеёиоуыэюя]+[^аеёиоуыэюя]*)$")
//        Matcher m = Pattern.compile("(.[аеёиоуыэюя]+)$")
        Matcher m = Pattern.compile("(...)$")
            .matcher(word
                .replaceAll("[ъь]","")
            );
        if(m.find())return reverse?new StringBuilder(m.group(1)).reverse().toString():m.group(1);
        return word;
    }

//    @Order
//    @JsonIgnore
//    public String getRevWord() {return new StringBuilder(this.word).reverse().toString();}

    @Order
    @JsonIgnore
    public int getSize() {return size;}

    @JsonIgnore
    public String getLetters() {
        return getLetters(true,true);
    }

    public String getLetters(boolean type) {
        return getLetters(type,false);
    }

    private String getLetters(boolean type,boolean reverse) {
        return (type)?getWord(reverse).replaceAll("[аеёиоуыэюя]",""):
            getWord(reverse).replaceAll("[^аеёиоуыэюя]",""); // "[бвгджзйклмнпрстфхцчшщъь]"
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

//    public int compareTo(Wordbook wb){return getEnd().compareTo(wb.getEnd());}
    public int compareTo(Wordbook wb){return word.compareTo(wb.word);}

    public String getWord(boolean reverse) {
        return new StringBuilder(getWord().replaceAll("[ьъ]","")).reverse().toString();
//        return (reverse)?new StringBuilder(getWord()).reverse().toString():getWord();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDescription(String desc) {
        description=desc;
    }

    public String getDescription() {
        return description;
    }
}
