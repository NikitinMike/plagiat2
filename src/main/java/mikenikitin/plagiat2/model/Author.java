package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.*;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.List;

//@AllArgsConstructor
//@Getter
//@Setter
//@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="AUTHOR_ID")
    private Long id;

    @Order
    private String name; // link to page

    private String realname;

    private Long rating; // good=0 >> poor=9999

//    @JsonIgnore
    @OneToMany (mappedBy="author")
    private List<Article> articles;

    public Author(String name) {
        this.name=name;
        rating=0L;
    }

    public Author(String name,String realname) {
        this.name=name;
        this.realname=realname;
        rating=0L;
    }

    public List<Article> getArticles() {return articles;}

    public String getName() {return name;}

    public String getRealname() {return realname;}
}
