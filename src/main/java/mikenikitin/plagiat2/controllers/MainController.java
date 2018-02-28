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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
public class MainController {

    private final String root = "http://www.stihi.ru", localHost = "http://localhost:8080",
            href = "<a href=(.+?)>", start = "http://www.stihi.ru/poems/list.html?topic=all";
    //"http://www.stihi.ru/poems/list.html?type=selected" //
    //"http://www.stihi.ru/poems/list.html?topic=all"

    private ArticleRepository articleRepository;

    private WordbookRepository wordbookRepository;

    private TextRepository textRepository;

    private AuthorRepository authorRepository;

    @RequestMapping("/stih")
    @ResponseBody
    private String stih(@RequestParam String url){return stih2base(url);}

    private static List<String> fulllst;

    @RequestMapping("/links/{level}")
    @ResponseBody
    private List<String> links(
        @RequestParam(defaultValue=start) String url,
        @PathVariable Long level, Model model
    ) {
//        model.addAttribute("list", findPoems(url,2));
//        model.addAttribute("root", localHost+"/day/?url="+root );
//        return "mainIndex";
        fulllst=new ArrayList<>();
        return findPoems(url,level);
    }


    @RequestMapping("/avtor")
    private String mainAuthors(@RequestParam(defaultValue=start) String url,Model model) {
        List<String> a=authors(url,localHost + "/avtor/?url=" + root);

        List<String> poems=poems(url,root);
        Integer c=poems.size();
        System.out.println("POEMS TO GO: "+c);
        for (String p:poems) System.out.println(--c+":"+!stih2base(p).isEmpty());
//        a.addAll(poems);

        model.addAttribute("list", a);
        return "avtors";
    }

    @RequestMapping("/")
    private String mainCatalog(Model model, @RequestParam(defaultValue=start) String url) {
        model.addAttribute("list", mainPage(url,""));
        model.addAttribute("root", localHost+"/day/?url="+root );
        return "mainIndex";
    }

    @RequestMapping("/day")
    private String day(@RequestParam(defaultValue=start) String url, Model model) {
        model.addAttribute("list", todayList(url,""));
        model.addAttribute("root", localHost+"/poems/?url="+root );
        return "dayIndex";
    }

    @RequestMapping("/today")
    private String today(@RequestParam(defaultValue=start) String url, Model model) {
        model.addAttribute("list", todayList(url,""));
        model.addAttribute("root", localHost+"/poems/?url="+root );
        return "todayIndex";
    }

    @RequestMapping("/poems")
    private String mainPoems(@RequestParam(defaultValue=start) String url,Model model) {
        List<String> poems=poems(url,root); // .subList(1,2);
        Integer c=poems.size();
        System.out.println("POEMS TO GO: "+c);
        for (String p:poems) System.out.println(--c+":"+!stih2base(p).isEmpty());
        model.addAttribute("list", poems);
        return "poems";
    }

    private List<String> mainPage(String url,String prefix) {
        List<String> ls = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        String s;
        while (m.find())
            if (Pattern.compile("month").matcher(m.group(1)).find())
                if (!ls.contains(s = Pattern.compile("\"").matcher(
                        Pattern.compile(" class=\".+\"").matcher(
                            m.group(1).replaceAll("&","%26")
                        ).replaceFirst("")
                    ).replaceAll(""))) ls.add(prefix+s);
//        Collections.sort(ls);
//        Collections.reverse(ls);
        return ls;
    }

    private List<String> todayList(String url,String prefix){
        System.out.println(url);
        List<String> today = new ArrayList<>(),days = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        while (m.find())
          if (Pattern.compile("poems").matcher(m.group(1)).find())
//                  && Pattern.compile("day").matcher(m.group(1)).find())
            if (Pattern.compile("start").matcher(m.group(1)).find()) // System.out.println(m.group(1));
                today.add(prefix+Pattern.compile("\"").matcher(
                    Pattern.compile("&").matcher(
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("%26") // "&amp;"
                ).replaceAll(""));
            else
                days.add(prefix+Pattern.compile("\"").matcher(
                    Pattern.compile("&").matcher(
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("%26") // "&amp;"
                ).replaceAll(""));
        Collections.reverse(today);
//        Collections.sort(days);
//        today.addAll(days);
        return today;
    }

    private List<String> authors(String url,String prefix){
        List<String> authors = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        while (m.find())
            if (!Pattern.compile("recomlink").matcher(m.group(1)).find())
                if (Pattern.compile("avtor").matcher(m.group(1)).find())
            // && Pattern.compile("authorlink").matcher(m.group(1)).find()
                    authors.add( Pattern.compile(prefix).matcher(m.group(1)).find()?"":prefix+Pattern.compile("\"").matcher(
                        Pattern.compile("&").matcher(
                                Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
//                            ).replaceAll("%26") // "&amp;"
                            ).replaceAll("&") // "&amp;"
                        ).replaceAll("")
                    );
//                else System.out.println(m.group(1));
        return authors;
    }

    private String getPage(String s){
//        System.out.println('['+s+']');
        StringBuilder result = new StringBuilder();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader((new URL(s).openConnection()).getInputStream(),"windows-1251")))
        {for(String line;(line=rd.readLine())!=null;) result.append(line); rd.close();}
        catch (IOException e) {e.printStackTrace();}
        return result.toString();
    }

    private String stripStih(String stih){ // <div class="copyright">
//        System.out.println(stih);
        Matcher m = Pattern.compile("<div class=\"text\">(.+?)</div>").matcher(stih);
        if (!m.find()) return "";
//        System.out.println(m.group(1));
        return Pattern.compile("&nbsp;|&quot;").matcher(m.group(1).replaceAll("<br>","\n")).replaceAll(" ");
    }

    private String stih2base(String url){

        try {url = URLDecoder.decode(url,"UTF-8");}
        catch (UnsupportedEncodingException e) {e.printStackTrace();}

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

        Article art= new Article(url);
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
        authorRepository.save(author);

        return stih;
//        return url;
    }

    private List<String> findPoems(String url,Long level) {
//        if (!Pattern.compile(root).matcher(url).find()) url=root+url;
//            url=URLDecoder.decode(root.concat(url),"UTF-8");

        fulllst.add(url);
        List<String> poems=poems(url,root);

        if (level>0)
            for (String lst:authors(url,root))
//            for (String lst:todayList(url,root))
//            for (String lst:mainPage(url,root))
                if (!fulllst.contains(lst))
                    poems.addAll(findPoems(lst,level-1));
//                    System.out.println(findPoems(lst,level-1));

        System.out.println(url+" :"+poems.size());

//        String page = getPage(url);
//        System.out.println(page);
//        Matcher m = Pattern.compile(href).matcher(page);
//        if (level>0) while (m.find()) // System.out.println(root+m.group(1));
//            poems.addAll(findPoems(Pattern.compile("/poems/list").matcher(m.group(1)).replaceFirst(root+"/poems/list"),level-1));

        return poems;

    }

    private List<String> poems(String url,String prefix) {
//        System.out.println(url);
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        List<String> poems = new ArrayList<>();
        while (m.find())
            if (Pattern.compile("poemlink").matcher(m.group(1)).find()) {
                poems.add( prefix + Pattern.compile("\"").matcher( // localHost + "/stih/?url=" +
                        Pattern.compile(" class=\".+\"").matcher(m.group(1)).replaceFirst("")
                        ).replaceAll("")
                );
            }
//        Collections.sort(poems);
        return poems;
    }

}
