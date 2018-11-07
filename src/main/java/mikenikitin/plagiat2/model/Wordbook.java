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

import static sun.swing.MenuItemLayoutHelper.max;

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

    private int accent;
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

        String parts[] = getParts().split("-");
        return parts[parts.length-1];

//        Matcher m = Pattern.compile("([аеёиоуыэюя]?[^аеёиоуыэюя]*[ъь]??[аеёиоуыэюя]+[^аеёиоуыэюя]*)$")
//        Matcher m = Pattern.compile("([аеёиоуыэюя]+[^аеёиоуыэюя]*)$")
//        Matcher m = Pattern.compile("(.[аеёиоуыэюя]+)$")

//        Matcher m = Pattern.compile("(...)$").matcher(word.replaceAll("[ъь]",""));
//        if(m.find())return reverse?new StringBuilder(m.group(1)).reverse().toString():m.group(1);
//        return word;
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

    public void setDescription(String description) {
        this.description=description;
//        accent=max(description.indexOf("`"),description.indexOf("'"));
//        accent=description.replaceFirst("[аеёиоуыэюя]?[`'][аеёиоуыэюя]?","@")
        accent=description.replaceFirst("'[аеёиоуыэюя]?","@")
            .replaceAll("[^@аеёиоуыэюя]","").indexOf("@")+1;
    }

    /* Пошаговое объяснение разбивки слова "список" на слоги - результат в конце страницы

    1 Слог начинается с согласного, который стоит перед гласной: мо-ло-ко, ко-ра
    2 Буквы ь, ъ (которые не означают звуков) нельзя отрывать от предыдущего слога: конь-ки, подъ-езд
    3 Глухие согласные отходят к следующему слогу, звонкие согласные ([й], [р], [р’], [л], [л’], [м], [м’], [н], [н’]) — к предыдущему слогу: то-чка, мо-шка, кор-ка, бул-ка
    4 Согласные буквы, образующие один звук, нельзя разносить в разные слоги. Один звук образуют сочетания зж [ж:], тся, ться [ц:]. у-е-зжать, но-си-тся, де-ла-ться
    5 Согласные в середине слова относят к следующему слогу: кла-ссный, хо-ккей, те-ннис

    Определяю границы слова с-п-и-с-о-к#
    #с#п#и-с-о-к
    Согласная + гласная (если есть согласная перед гласной, то она относется к этому слогу)
    Количество слогов = количеству гласных : спи-с#о-к
    Все согласные прилепляю к последнему слогу : спи-со#к
    */

    public String getParts(){
        String w="";
//        String g="аеёиоуыэюя";g+=g.toUpperCase();

        String parts[] = word.split("(?<=[^аеёиоуыэюя]?[аеёиоуыэюя])"); // ?
        int l=parts.length-1;
        if(l>0&&parts[l].replaceAll("[^аеёиоуыэюя]","").isEmpty())
            {parts[l-1]+=parts[l]; parts[l]="";}

        for (String p:parts) {
//            String s[] = p.split("(?<=[йрлмн][^аеёиоуыэюя])"); //
//            if(s.length>1)System.out.print(s[0]+"-"+s[1]+" ");
            String ps[] = p.split("(?<=[ьъ])");
            if(ps.length>1) w+=ps[0]+((w.isEmpty())?"":"-")+ps[1];
            else if(!p.isEmpty()) w+=((w.isEmpty())?"":"-")+p;
        }

        return w;
    }

    public String getDescription() {
//        String g="аеёиоуыэюя"; // g+=g.toUpperCase();
        if (description==null) return "";
        if (description.replaceAll("[аеёиоуыэюя]","")==description) return description;
//            return description.replaceAll("[`']","");
        if(description.indexOf("ё")>0) return description.replaceFirst("ё","Ё");
//        Matcher m=Pattern.compile("([`'][аеёиоуыэюя]|[аеёиоуыэюя][`'])").matcher(description);
//        Matcher m=Pattern.compile("([аеёиоуыэюя]?[`'][аеёиоуыэюя]?)").matcher(description);
        Matcher m=Pattern.compile("([аеёиоуыэюя])'").matcher(description);
        if(m.find()) return description.replaceFirst(m.group(1),m.group(1).toUpperCase());//.replaceAll("'","")); // [`']
        return description;
    }
}
