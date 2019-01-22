package mikenikitin.plagiat2.controllers;

//import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Author;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.AuthorRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
//@AllArgsConstructor
public class WikiController {

    private final String root = "http://www.stihi.ru", localHost = "http://localhost:8080",
            href = "<a href=(.+?)>", start = "http://www.stihi.ru/poems/list.html?topic=all";
    //"http://www.stihi.ru/poems/list.html?type=selected" //
    //"http://www.stihi.ru/poems/list.html?topic=all"

    private ArticleRepository articleRepository;

    private WordbookRepository wordbookRepository;

    private TextRepository textRepository;

    private AuthorRepository authorRepository;

    @RequestMapping("/demo")
    private String demo(@RequestParam(defaultValue = start) String url)
//        @RequestParam(required=false) Integer year,
//        @RequestParam(required=false) Integer month,
//        @RequestParam(required=false) Integer day
//        System.out.printf(" %d-%d-%d ",year,month,day);
//        if (year!=null&&month!=null&&day!=null) System.out.println(LocalDate.of(year,month,day));
    {
//        String catalog = getPage("http://www.stihi.ru/poems/list.html?day=20&month=02&year=2018&topic=all");
//        System.out.println(stihiStrip(getPage("http://www.stihi.ru/2016/10/28/1984")));
//        return getLinks(catalog).toString();
        return getPage(url);
    }

    @RequestMapping("/wiki")
    private Article wikipedia() throws Exception {

        StringBuilder result = new StringBuilder();
        URL url = new URL("https://ru.wikipedia.org/wiki/%D0%A1%D0%BB%D1%83%D0%B6%D0%B5%D0%B1%D0%BD%D0%B0%D1%8F:%D0%A1%D0%BB%D1%83%D1%87%D0%B0%D0%B9%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        for(String line;(line=rd.readLine())!=null;) result.append(line); rd.close();

        Article art=new Article(URLDecoder.decode(conn.getURL().toString(),"UTF-8"));
        articleRepository.save(art);

        List<Text> text = new ArrayList<>();
        Long wc=0L;
        for (String word:result.toString().replaceAll("[^а-яёА-ЯЁ]"," ").split("\\s+")) // \\p{Alpha}
            if (word.length()>2) {
                Wordbook wbr=wordbookRepository.findByWord(word.toLowerCase());
                if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word.toLowerCase()));
                text.add(new Text(art,null,wbr,++wc));
            }
//                System.out.print(++wc+":"+w.toLowerCase()+" ");
        System.out.println();
//        wordbookRepository.saveAll(wordbook);
        textRepository.saveAll(text);
//        System.out.println("Words:"+ wordbook);

//        art.setWc(wc);
        System.out.println(wc);
        articleRepository.save(art);

//        System.out.println("ArticlesList:"+ articleRepository.findAll());
//        for (Article article: articleRepository.findAll()) System.out.println(article);
//        articleRepository.findAll().forEach((b) -> System.out.println(b));
//        articleRepository.findAll().forEach((b) -> articles.add(b));

        return art;
    }

    private String getPage(String s){
        StringBuilder result = new StringBuilder();
        try (BufferedReader rd = new BufferedReader(
            new InputStreamReader((new URL(s).openConnection()).getInputStream(),"windows-1251")))
        {for(String line;(line=rd.readLine())!=null;) result.append(line);}
        catch (IOException e) {e.printStackTrace();}
        return result.toString();
    }

}
