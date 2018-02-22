package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    WordbookRepository wordbookRepository;

    @RequestMapping("/rest")
    @ResponseBody
    private List<Wordbook> listall(){
        return wordbookRepository.findAll();
    }

    @RequestMapping({"/index", "/"})
    private String index(Model model) {
        model.addAttribute("wordbook", wordbookRepository.findAll());
        return "indexWordBook";
    }
}
