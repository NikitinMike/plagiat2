package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mikenikitin.plagiat2.repository.WordbookRepository;
import mikenikitin.plagiat2.services.Combiner;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@AllArgsConstructor
@Getter
@Setter
//@NoArgsConstructor
@Entity

public class Clause {

//    private WordbookRepository wordbookRepository;

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

    private String clause;
    private Long wc;
    private Integer parts;

    //    private String end;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="WORDBOOK_ID")
    @JsonIgnore
    private Wordbook end;

    @JsonIgnore
    @OneToMany (mappedBy="clause")
    private List<Text> text;

    public String getTextString(){
        String predlogi=" да ж что а но в без до из к на по о от перед уже при через с у за над об под про для как не ни ";
//        String pred[]=predlogi.split(" ");
        String comb="";
        for (Text t:text) {
            String w=t.getWord().getDescription();
//            System.out.println("["+w+"]");
            if(w.isEmpty())w=t.getWord().getWord();
            comb += (predlogi.indexOf(t.getWord().getWord()) >= 0)? w+"_":w+" ";
        }
//            System.out.print("["+t.getWord().getWord()+"]");
//            System.out.print(predlogi.indexOf(t.getWord().getWord()));
//        System.out.println();
        if(text.size()>9) return comb.replaceAll("_"," ");
        if(comb.split(" ").length>2)
            return new Combiner(comb).randomOut().replaceAll("_"," ");
        else return comb.replaceAll("_"," ");
    }

    public Clause() {}
    public Clause(Article art) {article=art;}

    public Clause(Clause clause,Article article,Long number) {
        this.article=article;
        this.number=number;
        this.clause=clause.clause;
        wc=clause.wc;
        parts=clause.parts;
        end=clause.end;
        text=clause.text;
    }

//    public Clause(Article art, List<Wordbook> list, Long pos){
    public Clause(Article art, String line, Long num){
        article=art;
        clause=line; // list.toString();
        wc=0L;
//        end=clause.trim().toLowerCase();
//        end=end.substring(end.length()-3);
        number=num;
//        clause=list.toString().replaceAll(",","");
        parts=clause.replaceAll("[^аяёоуыиеэюАЯЁОУЫИЕЭЮaouieAOUIE]","").length();
/*
        Long cwc = 0L;
//        Text clauseEnd = null;
        String[] linewords = line.toLowerCase().replaceAll("[^а-яёa-z]", " ").trim().split("\\s+");
//        if (linewords.length < 1||linewords.length > 250) continue;
        for (String word : linewords) // \\p{Alpha}
            if (word.length() > 0)
                try {
                    Wordbook wbr = wordbookRepository.findByWord(word);
                    if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word));
                    // clause.addWord(wbr, ++wc);
//                    text.add(new Text(art, id, wbr, ++cwc));
                    wc++;
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " [" + word + "] ");
                }
//        if (clauseEnd != null) clause.setEnd(clauseEnd.setClause(true));
  */
    }


    public void setEnd(Wordbook end){this.end=end;}
//    public String getEnd(){ return text.get(text.size()-1).getWord().getEnd();}
//    @Override
    public Wordbook getEnd(){ return end;}
//    @Override
//    public String getEndS(){ return end.getEnd();}

    public void addWord(Wordbook wbr,long position){
//        this.position=position;
//        System.out.println(wbr);
        wc++;
    }

    public void setWc(Long wc) {
        this.wc = wc;
    }
}
