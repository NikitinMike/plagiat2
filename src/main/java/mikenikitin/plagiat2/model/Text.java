package mikenikitin.plagiat2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
//@AllArgsConstructor
//@Getter
//@Setter
public class Text {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
//    @Column(name="TEXT_ID")
    private Long id;

//    @JsonIgnore
    private Long position;

    private boolean lineBreak; // end clause=true

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Article article;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Clause clause;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="WORDBOOK_ID")
    @JsonIgnore
    private Wordbook word;

    public Text(Article article, Clause clause, Wordbook word, Long position){
        this.article=article;
        this.clause=clause;
        this.word=word;
        this.position=position;
    }

    public Article getArticle() {return article;}

    public Wordbook getWord() {
        return word;
    }

    public Wordbook setClause(boolean clause) {this.lineBreak=clause; return clause?word:null;}

}
