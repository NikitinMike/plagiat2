package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ARTICLE_ID")
    private Long id;

    @Order
    private String name;
    private Long wc;

    @JsonIgnore
    @OneToMany // (mappedBy="article")
    @JoinColumn(name="TEXT_ID", referencedColumnName="ARTICLE_ID") // nullable = false
    private List<Text> text;

    public Article(String name, Long wc) {
        this.name = name;
        this.wc=wc;
    }

    public Article(String name) {
        this.name = name;
        wc=0L;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<Text> getText() {
        return text;
    }

    public Long getWc() {
        return wc;
    }

    public void setWc(Long wc) {
        this.wc = wc;
    }

//    public void setwords(List<String> words){
//        this.words=words;
//    }

}
