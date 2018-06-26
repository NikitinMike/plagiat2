package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Clause;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.ClauseRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("clause")

public class ClauseController {

    private WordbookRepository wordbookRepository;
    private TextRepository textRepository;
    private ArticleRepository articleRepository;
    private ClauseRepository clauseRepository;

    @RequestMapping({"/page"})
//    @ResponseBody
//    private Page<Clause> pageable(Model model,
    private String pageable(Model model,
//          @SortDefault.SortDefaults(@SortDefault(sort = "word", direction = Sort.Direction.ASC))
         @PageableDefault(size = 99) Pageable pageable)
    {
        int pagesCount=clauseRepository.findAll().size()/pageable.getPageSize();
        List<Integer> pages=new ArrayList<>();
        for (int i = 0; i <= pagesCount; i++) pages.add(i);
        model.addAttribute("pages",pages);
        model.addAttribute("clauses", clauseRepository.findAll(pageable)); // .getContent() pageable = updatePageable(pageable,999)
        return "Clauses";
//        return clauseRepository.findAll(pageable);
    }

}
