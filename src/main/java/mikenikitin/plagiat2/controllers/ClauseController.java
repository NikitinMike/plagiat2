package mikenikitin.plagiat2.controllers;

//import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Clause;
import mikenikitin.plagiat2.repository.ClauseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@AllArgsConstructor
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

    @RequestMapping({"/parts/{parts}"})
    private String ends(Model model,@PathVariable Integer parts,
        @SortDefault.SortDefaults({
//            @SortDefault(sort = "parts", direction = Sort.Direction.DESC),
            @SortDefault(sort = "end", direction = Sort.Direction.ASC),
        }) @PageableDefault(size = 1000) Pageable pageable)
    {
        model.addAttribute("pages",clauseRepository.findAllByPartsEquals(parts).size()/pageable.getPageSize());
        Page<Clause> clauses = clauseRepository.findAllByPartsEquals(pageable,parts);
//        List<Clause> clauses = clauseRepository.findAllByPartsEquals(parts); // .stream().distinct().collect(Collectors.toList());
//        clauses.sort((c1, c2)->{return c1.getEnd().compareTo(c2.getEnd());});
//        clauses.removeIf(clause -> {return clauses.contains(clause);});
        model.addAttribute("clauses",clauses);
        model.addAttribute("parts",parts);
        return "ClauseEnds";
    }

    @RequestMapping({"/parts"})
    private String ends(Model model
//        @SortDefault.SortDefaults({
//            @SortDefault(sort = "parts", direction = Sort.Direction.DESC),
//            @SortDefault(sort = "end", direction = Sort.Direction.ASC),
//        }) @PageableDefault(size = 1000) Pageable pageable
    ){
        model.addAttribute("pages", 1); // clauseRepository.count()/pageable.getPageSize());
        model.addAttribute("clauses", null); // clauseRepository.findAll(pageable));
        model.addAttribute("parts",30);
        return "ClauseParts";
    }

}
