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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long position;

    @Column(name="TEXT_ID")
    @JsonIgnore
    private Long articleId;

//    @Column(name="WORD_ID")
//    @JsonIgnore
//    private Long wbId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="WORDBOOK_ID")
    private Wordbook word;

    public Text(Article art, Wordbook word, Long position){
        this.position=position;
        this.word=word;
//        this.wbId=word.getId();
        this.articleId=art.getId();
    }

    public Wordbook getWord() {
        return word;
    }
}
