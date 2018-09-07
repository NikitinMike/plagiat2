package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.repository.ClauseRepository;
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

    private ClauseRepository clauseRepository;

    @RequestMapping({"/page"})
//    @ResponseBody
//    private Page<Clause> page(Model model,
    private String page(Model model,
        @SortDefault.SortDefaults({
            @SortDefault(sort = "article", direction = Sort.Direction.DESC),
            @SortDefault(sort = "number", direction = Sort.Direction.ASC),
        }) @PageableDefault(size = 1000) Pageable pageable)
    {
//        int pagesCount=clauseRepository.findAll().size()/pageable.getPageSize();
//        List<Integer> pages=new ArrayList<Integer>() {{for (int i = 0; i <= pagesCount; i++) add(i); }};
        model.addAttribute("pages",clauseRepository.count()/pageable.getPageSize());
        model.addAttribute("clauses", clauseRepository.findAll(pageable));
        // .getContent() pageable = updatePageable(pageable,999)
        return "Clauses";
//        return clauseRepository.findAll(pageable);
    }

    @RequestMapping({"/ends"})
//    @ResponseBody
//    private Page<Clause> pageable(Model model,
    private String ends(Model model,
        @SortDefault.SortDefaults({
            @SortDefault(sort = "parts", direction = Sort.Direction.DESC),
            @SortDefault(sort = "end", direction = Sort.Direction.ASC),
        }) @PageableDefault(size = 1000) Pageable pageable)
    {
//        int pagesCount=clauseRepository.findAll().size()/pageable.getPageSize();
//        List<Integer> pages=new ArrayList<Integer>() {{for (int i = 0; i <= pagesCount; i++) add(i); }};
        model.addAttribute("pages",clauseRepository.count()/pageable.getPageSize());
        model.addAttribute("clauses", clauseRepository.findAll(pageable));
        // .getContent() pageable = updatePageable(pageable,999)
        return "ClauseEnds";
//        return clauseRepository.findAll(pageable);
    }

}
