package mikenikitin.plagiat2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
//@NoArgsConstructor
@Entity
public class SpeechPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    // существительное,прилагательное,числительное,местоимение,наречие,глагол,причастие,деепричастие,предлоги,союзы,частицы,междометия
    private String name;
}
