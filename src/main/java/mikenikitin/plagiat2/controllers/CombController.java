package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.services.Combiner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping({"comb","combiner"})

public class CombController {

    //    @ResponseBody
    @RequestMapping({"/","/order/","/page"}) //
    private String index(Model model
//         ,@SortDefault.SortDefaults({
//            @SortDefault(sort="article", direction=Sort.Direction.ASC),@SortDefault(sort="number", direction=Sort.Direction.ASC)
//         }) @PageableDefault(size = 100) Pageable pageable
    ) {

        String text1[]={
                "у_попа была собака",
                "он её любил",
                "она съела кусок мяса",
                "он её убил",
                "в_землю закопал",
                "надпись написал"
        };
        String text2[]={
                "я поэт зовут незнайка",
                "от_меня вам балалайка"
        };
        String text3[]={
                "в_траве сидел кузнечик",
                "совсем как огуречик",
                "представьте себе",
                "представьте себе",
                "зелёненький он был"
        };

//        String[] catNames = new String[10];
//        String[] catsNames = {"Васька","Кузя","Барсик","Мурзик","Леопольд","Бегемот","Рыжик","Матроскин"};
        String catNames = "Васька Кузя Барсик Мурзик Леопольд Бегемот Рыжик Матроскин";
//        Combiner c=new Combiner(catNames);c.out();System.out.println();
//        model.addAttribute("list",catNames);

        ArrayList<String> text = new ArrayList<>();
        for(String a:text1) text.add(new Combiner(a).randomOut());
        model.addAttribute("list",text);
        return "combiner";
    }

}