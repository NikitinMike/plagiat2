package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    WordbookRepository wordbookRepository;

    @RequestMapping()
    private List<Wordbook> listall(){
        return wordbookRepository.findAll();
    }


}
