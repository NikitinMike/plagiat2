package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

//@AllArgsConstructor
@Getter
@Setter
//@NoArgsConstructor
@Entity

public class Clause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="CLAUSE_ID")
    private Long id;

    //    @JsonIgnore
    private Long position;

    private String clause;
//    private Integer parts;

//    @OneToMany // (mappedBy="author")
//    private List<Wordbook> clause;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Article article;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn // (name="WORDBOOK_ID")
//    @JsonIgnore
//    private Wordbook word;

//    @JsonIgnore
//    @OneToMany(mappedBy="article")
//    @JoinColumn(name="TEXT_ID", referencedColumnName="ARTICLE_ID") // nullable = false
//    private List<Text> text;

//    public Clause(Article art, List<Wordbook> list, Long pos){
    public Clause(Article art, String list, Long pos){
        article=art;
        clause=list; // list.toString();
        position=pos;
//        clause=list.toString().replaceAll(",","");
//        parts=clause.replaceAll("[^аяёоуыиеэюАЯЁОУЫИЕЭЮ]","").length();
    }

}
