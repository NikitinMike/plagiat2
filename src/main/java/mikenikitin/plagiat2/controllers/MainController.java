package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor
public class MainController {

    private ArticleRepository articleRepository;

    private WordbookRepository wordbookRepository;

    private TextRepository textRepository;

    @RequestMapping("/demo")
    private String demo(
        @RequestParam(defaultValue = "http://www.stihi.ru/poems/list.html?topic=all") String url
    ) throws Exception {
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

//        List<Wordbook> wordbook = new ArrayList<>();
//        wordbook=wordbookRepository.findAll();
//        System.out.println("WordBook:"+wordbook);

        List<Text> text = new ArrayList<>();
        Long wc=0L;
        for (String word:result.toString().replaceAll("[^а-яёА-ЯЁ]"," ").split("\\s+")) // \\p{Alpha}
            if (word.length()>2) {
                Wordbook wbr=wordbookRepository.findByWord(word.toLowerCase());
                if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word.toLowerCase()));
                // wordbook.add(new Wordbook(w.toLowerCase(),++wc));
                text.add(new Text(art,wbr,++wc));
//                text.add(new Text(++wc));
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

    @RequestMapping("/")
    private List<String> getLinks(
//        @PathVariable String path,
//      "http://www.stihi.ru/poems/list.html?type=all" // selected
        @RequestParam(defaultValue = "http://www.stihi.ru/poems/list.html?topic=all" ) String url
//        @RequestParam(required=false) Integer year,
//        @RequestParam(required=false) Integer month,
//        @RequestParam(required=false) Integer day
    ) throws Exception {
//        System.out.println(path);
        System.out.println(url);
//        System.out.printf(" %d-%d-%d ",year,month,day);
//        if (year!=null&&month!=null&&day!=null) System.out.println(LocalDate.of(year,month,day));
        String root="http://www.stihi.ru",localHost="http://localhost:8080";
        Matcher m = Pattern.compile("<a href=(.+?)>").matcher(getPage(url));
        List<String> ls=new ArrayList(),poems=new ArrayList(),authors=new ArrayList();
        ls.add(localHost);
        while (m.find())
            if (!Pattern.compile(root).matcher(m.group(1)).find())
                if (Pattern.compile("poemlink").matcher(m.group(1)).find()) {
                    String s= Pattern.compile("\"").matcher(
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("");
                    stihi2base(root+s);
                    System.out.println(root+s);
                    poems.add(localHost+"/stih?url="+root+s);
                } else {
                    if (Pattern.compile("authorlink").matcher(m.group(1)).find()) // System.out.println(m.group(1));
                        authors.add(localHost+"/?url=" +root+
                            Pattern.compile("\"").matcher(
                                Pattern.compile("&").matcher(
                                    Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                                ).replaceAll("%26") // "&amp;"
                            ).replaceAll("")
                        );
                    else ls.add(localHost+"/?url=" +root+
                        Pattern.compile("\"").matcher(
                            Pattern.compile("&").matcher(
                                Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                            ).replaceAll("%26") // "&amp;"
                        ).replaceAll("")
                    );
                }
//        ls.addAll(stihi);

//        if (!url.isEmpty());

        if (authors.isEmpty()) return ls;
        authors.add(localHost);
        Collections.reverse(authors);
        return authors;
    }

    private String getPage(String s) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(s).openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"windows-1251"));
        StringBuilder result = new StringBuilder();
        for(String line;(line=rd.readLine())!=null;) result.append(line); // System.out.println(line)
        rd.close();
//        System.out.println(result);
        return result.toString();
    }

    private String stihiStrip(String url) throws Exception { // <div class="copyright">
        Matcher m = Pattern.compile("<div class=\"text\">(.+?)</div>").matcher(getPage(url));
        if (m.find()) return Pattern.compile("&nbsp;|&quot;")
            .matcher(Pattern.compile("<br>").matcher(m.group(1)).replaceAll("\n"))
            .replaceAll(" ");
        return "";
    }

    private String stihi2base(String url) throws Exception {

        Article art=new Article(URLDecoder.decode(url,"UTF-8"));
        if (articleRepository.findArticlesByName(art.getName())!=null) return "";
        articleRepository.save(art);

//        System.out.println(url);
        String stih=stihiStrip(url);

        Long wc=0L;
        List<Text> text = new ArrayList<>();
        for (String word:stih.replaceAll("[^а-яёА-ЯЁ]"," ").split("\\s+")) // \\p{Alpha}
            if (word.length()>0) {
                Wordbook wbr=wordbookRepository.findByWord(word.toLowerCase());
                if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word.toLowerCase()));
                text.add(new Text(art,wbr,++wc));
            }
        textRepository.saveAll(text);

        System.out.println(wc);
        art.setWc(wc);
        articleRepository.save(art);

        return stih;
//        return url;
    }

    @RequestMapping("/stih")
    private String stih(@RequestParam String url) throws Exception {
        return stihi2base(url);
    }

}
