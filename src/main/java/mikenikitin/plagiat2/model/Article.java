package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="ARTICLE_ID")
    private Long id;

    @Order
    private String name; // link to article
    private String title;
    private Long wc;
    private Long rating; // good=0 >> poor=9999

    @JsonIgnore
    @OneToMany (mappedBy="article")
//    @JoinColumn(name="TEXT_ID", referencedColumnName="ARTICLE_ID") // nullable = false
    private List<Text> text;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="AUTHOR_ID")
    @JsonIgnore
    private Author author;

    public Article(String name, Long wc) {
        this.name = name;
        this.wc=wc;
        this.rating=0L;
    }

    public Article(String name) {
        this.name = name;
        this.rating=0L;
        wc=0L;
    }

    public Article(String name,Author author) {
        this.name = name;
        this.author = author;
        this.rating=0L;
        wc=0L;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setWc(Long wc) {
        this.wc = wc;
    }

    public Long getId() {
        return id;
    }

    public Long getWc() {
        return wc;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public List<Text> getText() {
        return text;
    }

//    public String getName() {
//        return name;
//    }

//    public Long getId() {return id;}

//    public List<Text> getText() {
//        return text;
//    }

//    public Long getWc() {
//        return wc;
//    }

//    public void setWc(Long wc) {
//        this.wc = wc;
//    }

//    public void setAuthor(Author author) {
//        this.author = author;
//    }

//    public void setTitle(String title) {this.title = title;}

//    public String getTitle() {return title;}

//    public void setwords(List<String> words){
//        this.words=words;
//    }

}
