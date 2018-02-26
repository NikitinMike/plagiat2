package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Author;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.AuthorRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final String root = "http://www.stihi.ru", localHost = "http://localhost:8080",
            href = "<a href=(.+?)>", start = "http://www.stihi.ru/poems/list.html?topic=all";
    //      "http://www.stihi.ru/poems/list.html?type=selected" //

    private ArticleRepository articleRepository;

    private WordbookRepository wordbookRepository;

    private TextRepository textRepository;

    private AuthorRepository authorRepository;

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

//    @RequestMapping("/{page}")
    @RequestMapping("/")
    private List<String> mainCatalog(
//        @PathVariable String page,
//        @RequestParam(required=false) Integer year,
//        @RequestParam(required=false) Integer month,
//        @RequestParam(required=false) Integer day
//        System.out.printf(" %d-%d-%d ",year,month,day);
        @RequestParam(defaultValue = "" ) String url
    ) throws Exception {
//        if (year!=null&&month!=null&&day!=null) System.out.println(LocalDate.of(year,month,day));
//        System.out.println(path);
//        System.out.println(url);
//        return today(url);
        return mainPage(url.isEmpty()?start:url);
    }

    private List<String> mainPage(String url) throws Exception {
        List<String> ls = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        String s;
        while (m.find())
            if (Pattern.compile("poems").matcher(m.group(1)).find())
                if (!Pattern.compile("start").matcher(m.group(1)).find())
                    if (!ls.contains(s = localHost + "/poems/?url=" + root +
                        Pattern.compile("\"").matcher(
                            Pattern.compile(" class=\".+\"").matcher(
                                m.group(1).replaceAll("&","%26")
                            ).replaceFirst("")
                        ).replaceAll(""))) ls.add(s);
        Collections.sort(ls);
        ls.add(localHost);
        ls.add(localHost+"/avtor");
        ls.add(localHost+"/poems");
        ls.add(localHost+"/today");
        ls.add(localHost+"/articles/");
        Collections.reverse(ls);
        return ls;
    }

    @RequestMapping("/today")
    private List<String> today(@RequestParam(defaultValue = "" ) String url) throws Exception
    { // System.out.println(url);
        return todayList(url.isEmpty()?start:url);
    }

    private List<String> todayList(String url) throws Exception {
        System.out.println(url);
        List<String> today = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        while (m.find())
            if (Pattern.compile("start").matcher(m.group(1)).find()) // System.out.println(m.group(1));
                today.add(localHost + "/poems/?url=" + root + Pattern.compile("\"").matcher(
                    Pattern.compile("&").matcher(
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("%26") // "&amp;"
                ).replaceAll(""));
        today.add(localHost);
        today.add(localHost+"/avtor");
        today.add(localHost+"/poems");
        today.add(localHost+"/today");
        today.add(localHost+"/articles/");
        Collections.reverse(today);
        return today;
    }

    @RequestMapping("/poems")
    private List<String> mainPoems(@RequestParam(defaultValue = "" ) String url) throws Exception {
        List<String> poems=poems(url.isEmpty()?start:url);
        Integer c=poems.size()-2;
        System.out.println("POEMS TO GO: "+c);
//        if (url.isEmpty())
        for (String p:poems) System.out.println(--c+":"+!stih2base(p).isEmpty());
        poems.add(localHost+"/poems");
        Collections.reverse(poems);
        return poems;
    }

    private List<String> poems(String url) throws Exception {
        System.out.println(url);
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        List<String> poems = new ArrayList<>();
        while (m.find())
            if (Pattern.compile("poemlink").matcher(m.group(1)).find()) {
                poems.add( // localHost + "/stih/?url=" +
                    root + Pattern.compile("\"").matcher(
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("")
                );
            }
        poems.add(localHost);
        poems.add(localHost+"/avtor");
        return poems;
    }

    @RequestMapping("/avtor")
    private List<String> mainAuthors(@RequestParam(defaultValue = "" ) String url) throws Exception {
        List<String> a=authors(url.isEmpty()?start:url);
        if (url.isEmpty()) return a;
        List<String> poems=poems(url);
        Integer c=poems.size()-2;
        System.out.println("POEMS TO GO: "+c);
        for (String p:poems) System.out.println(--c+":"+!stih2base(p).isEmpty());
        a.addAll(poems);
        return a;
    }

    private List<String> authors(@RequestParam(defaultValue = "" ) String url) throws Exception {
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        List<String> authors = new ArrayList<>(); authors.add(localHost+"/avtor"); authors.add(localHost);
//        String urls=Pattern.compile(root).matcher(url).replaceFirst("");
//        System.out.println(url);
        while (m.find())
            if (!Pattern.compile("recomlink").matcher(m.group(1)).find())
                if (Pattern.compile("avtor").matcher(m.group(1)).find())
            // && Pattern.compile("authorlink").matcher(m.group(1)).find()
                    authors.add(localHost + "/avtor/?url=" + root +
                        Pattern.compile("\"").matcher(
                            Pattern.compile("&").matcher(
                                Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                            ).replaceAll("%26") // "&amp;"
                        ).replaceAll("")
                    );
//                else System.out.println(m.group(1));
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

    private String stripStih(String stih) throws Exception { // <div class="copyright">
//        System.out.println(stih);
        Matcher m = Pattern.compile("<div class=\"text\">(.+?)</div>").matcher(stih);
        if (!m.find()) return "";
//        System.out.println(m.group(1));
        return Pattern.compile("&nbsp;|&quot;").matcher(m.group(1).replaceAll("<br>","\n")).replaceAll(" ");
    }

    private String stih2base(String url) throws Exception {

//        System.out.println(url);
        String stih=getPage(url);

        String title=localHost;
        Matcher tm = Pattern.compile("<h1>(.+?)</h1>").matcher(stih);
        if (tm.find()) title=tm.group(1);

        Matcher am = Pattern.compile("<a href=\"(.+?)\">(.+?)</a>").matcher(stih);
        String authorName=am.find()?root+am.group(1):localHost;
        String realName=am.find()?am.group(2):localHost;

        stih=stripStih(stih);
        if(stih.isEmpty())return "";

        String[] words=stih.replaceAll("[^а-яёА-ЯЁ]"," ").split("\\s+");
        if(words.length>999||words.length<9) return "";

        Article art=new Article(URLDecoder.decode(url,"UTF-8"));
        if (articleRepository.findArticlesByName(art.getName())!=null) return "";

        Author author=authorRepository.findByName(authorName);
        if (author==null) authorRepository.save(author=new Author(authorName,realName));

        art.setAuthor(author);
        art.setTitle(title);

        System.out.println(url);
        System.out.print(" length:"+stih.length());
        System.out.print(" words:"+words.length);

        Long wc=0L;
        List<Text> text = new ArrayList<>();
        articleRepository.save(art);

        List<String> nw=new ArrayList<>(); // new words in wordbook
        for (String word:words) // \\p{Alpha}
            if (word.length()>0) {
                Wordbook wbr=wordbookRepository.findByWord(word.toLowerCase());
                if (wbr == null) System.out.print(".");
                if (wbr == null) nw.add(word.toLowerCase());
                if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word.toLowerCase()));
                text.add(new Text(art,wbr,++wc));
            }
        textRepository.saveAll(text);

        System.out.println(" WC:"+wc);
        System.out.println(nw);
        art.setWc(wc);
        articleRepository.save(art);

        return stih;
//        return url;
    }

    @RequestMapping("/stih")
    private String stih(@RequestParam String url) throws Exception {
        return stih2base(url);
    }

}
