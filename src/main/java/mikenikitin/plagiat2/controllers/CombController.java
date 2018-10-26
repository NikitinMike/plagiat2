package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping({"comb","combiner"})

public class CombController {

    public class Combiner {

        int amount=0;
        int [][] comb;
        String[] initseq;

        int[] swap(int in[],int a,int b){
            int[]out=in.clone();
            out[b]=in[a];out[a]=in[b];
            return out;
        }

        String out(int[] a){
            String s="";
            for (int i=0;i<initseq.length;i++ )
                s = s+initseq[a[i]]+' ';
            return s;
        }

        int combiner(int n){
            if (n>2) {
                int nf=combiner(n-1);
                for(int i=0;i<nf;i++)
                    for(int j=1;j<n;j++)
                        comb[nf*j+i]=swap(comb[nf*(j-1)+i],n-j,n-j-1);
                return nf*n;
            }
            // N=2
            comb[1]=swap(comb[0],0,1);
            return 2;
        }

        Combiner(String str){
            initseq = str.split(" ");
            comb=new int[factorial(initseq.length)][initseq.length];
            for (int i=0;i<initseq.length;i++)comb[0][i]=i;
            amount=combiner(initseq.length);
        }

        String randomOut(){
            int r = (int) (Math.random()*amount);
            return out(comb[r]);
        }

        int factorial(int n) {
            if (n>0) return factorial(n-1)*n;
            return 1;
        }

        void out()
        {
//            System.out.println(comb.length);
            for(int i=0;i<comb.length;i++)
                System.out.println(i+" : "+out(comb[i]));
        }

    }

    //    @ResponseBody
    @RequestMapping({"/","/order/","/page"}) //
    private String index(Model model
//         ,@SortDefault.SortDefaults({
//            @SortDefault(sort="article", direction=Sort.Direction.ASC),@SortDefault(sort="number", direction=Sort.Direction.ASC)
//         }) @PageableDefault(size = 100) Pageable pageable
    ) {
//        String[] catNames = new String[10];
        String[] catsNames = {"Васька","Кузя","Барсик","Мурзик","Леопольд","Бегемот","Рыжик","Матроскин"};
        String catNames = "Васька Кузя Барсик"; //" Мурзик Леопольд Бегемот Рыжик Матроскин";

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

//        Combiner c=new Combiner(catNames);c.out();System.out.println();
        
        ArrayList<String> text = new ArrayList<>();
        for(String a:text1)
            text.add(new Combiner(a).randomOut());

//        model.addAttribute("list",catsNames);
        model.addAttribute("list",text);
        return "combiner";
    }

}