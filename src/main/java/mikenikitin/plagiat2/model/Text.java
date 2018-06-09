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

    private boolean clause; // end clause=true

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Article article;

//    @Column(name="WORD_ID")
//    @JsonIgnore
//    private Long wbId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="WORDBOOK_ID")
    @JsonIgnore
    private Wordbook word;

    public Text(Article art, Wordbook word, Long position){
        this.article=art;
        this.word=word;
        this.position=position;
        clause=false;
//        this.wbId=word.getId();
//        this.articleId=art.getId();
    }

    public Article getArticle() {
        return article;
    }

    public Wordbook getWord() {
        return word;
    }

    public void setClause(boolean clause) {this.clause=clause;}

//    public Wordbook getWord() {
//        return word;
//    }
}
