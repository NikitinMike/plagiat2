package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

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
//    @Order
//    @OrderColumn
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Article article;

    //    @JsonIgnore
    private Long number;

    private Integer parts;

    private String clause;
    private String end;

    @JsonIgnore
    @OneToMany (mappedBy="clause")
    private List<Text> text;

    public Clause() {}
    public Clause(Article art) {article=art;}

//    public Clause(Article art, List<Wordbook> list, Long pos){
    public Clause(Article art, String list, Long num){
        article=art;
        clause=list; // list.toString();
        number=num;
//        clause=list.toString().replaceAll(",","");
        parts=clause.replaceAll("[^аяёоуыиеэюАЯЁОУЫИЕЭЮaouieAOUIE]","").length();
        end = text.get(text.size()-1).getWord().getEnd();
    }

    public void addWord(Wordbook wbr,long position){
//        this.position=position;
//        System.out.println(wbr);
    }

}
