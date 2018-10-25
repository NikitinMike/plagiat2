package mikenikitin.plagiat2.controllers;


import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.repository.ArticleRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping({"comb","combiner"})
public class Combiner {

    private ArticleRepository articleRepository;

//    @ResponseBody
    @RequestMapping({"/","/order/","/page"}) //
    private String index(Model model
//         ,@SortDefault.SortDefaults({
//            @SortDefault(sort="article", direction=Sort.Direction.ASC),@SortDefault(sort="number", direction=Sort.Direction.ASC)
//         }) @PageableDefault(size = 100) Pageable pageable
    ) {
//        String[] catNames = new String[10];
        String[] catsNames = {
                "Васька",
                "Кузя",
                "Барсик",
                "Мурзик",
                "Леопольд",
                "Бегемот",
                "Рыжик",
                "Матроскин"
        };
        model.addAttribute("list",catsNames);
        return "combiner";
    }

}
