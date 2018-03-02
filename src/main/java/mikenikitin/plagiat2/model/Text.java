package mikenikitin.plagiat2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Text {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonIgnore
//    @Column(name="TEXT_ID")
    private Long id;

//    @JsonIgnore
    private Long position;

//    private Long articleId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
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
//        this.wbId=word.getId();
//        this.articleId=art.getId();
    }

    public Article getArticle() {
        return article;
    }

//    public Wordbook getWord() {
//        return word;
//    }
}
