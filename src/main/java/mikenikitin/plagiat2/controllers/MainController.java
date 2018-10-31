package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.*;
import mikenikitin.plagiat2.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.aspectj.util.LangUtil.split;

@Controller
@AllArgsConstructor
public class MainController {

    private ArticleRepository articleRepository;

    private WordbookRepository wordbookRepository;

    private TextRepository textRepository;

    private AuthorRepository authorRepository;

    private ClauseRepository clauseRepository;

    @RequestMapping("/wbcheck")
    @ResponseBody
    public void wordbookcheck(){
//      List<Wordbook> wblist = wordbookRepository.findAll();
//      wblist.sort((a,b)->(a.getWord().compareTo(b.getWord())));
//      for (Wordbook word:wblist) System.out.print(word.getWord()+" ");
        Map<String, Wordbook> map = new HashMap <>();
        for (Wordbook w:wordbookRepository.findAll()) map.put(w.getWord(),w);
//      map.forEach((key,word)-> System.out.print(word.getWord()+" "));
        System.out.println(map.size());
    }

    @RequestMapping("/wbload")
    @ResponseBody
    public int readFileLineByLine(
        @RequestParam(name = "file", defaultValue = "wordbook.txt") String fileName
    ) {
        System.out.println("loading WORDBOOK "+fileName);

        Map<String, Wordbook> map = new HashMap <>();
        for (Wordbook wb:wordbookRepository.findAll()) map.put(wb.getWord(),wb);
        System.out.println(map.size());

        int ln=0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while (line != null) {
//                System.out.println(line);
                System.out.print("*");
                String words[] = line.toLowerCase().split("#")[1].split(",");
                for (String str:words)
                {
                    Wordbook wb=map.get(str.replaceAll("[^а-яё]",""));
                    if (wb==null) continue;
//                    if (wb.getDescription()!=null&&wb.getDescription()!="") continue;
//                    System.out.print(word+" ");
//                    wb = wordbookRepository.findByWord(word);
//                    if (wb == null) continue;
//                    if (wb.getDescription()==null||wb.getDescription()=="")
                    {
                        System.out.print(str);
                        wb.setDescription(str);
                        wordbookRepository.save(wb);
                        System.out.print(" ");
                        ln++;
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {e.printStackTrace();}
        System.out.println();
        System.out.println(ln);
        return ln;
    }

    @RequestMapping("/lopatin")
    @ResponseBody
    private int WordBook(
        @RequestParam(name = "file", defaultValue = "lop1v2.utf8.txt") String file
    ){
        System.out.println("loading LOPATIN "+file);
        List<String> lines = Collections.emptyList();
        try { lines = Files.readAllLines(Paths.get(file), UTF_8);
        } catch (IOException e) {e.printStackTrace();}
        for (String line:lines) {
            String word[] = line.split("#");
            Wordbook wbr = wordbookRepository.findByWord(word[0]);
            if (wbr != null) {
                wbr.setDescription(word[1].split("%")[0]); // %
                wordbookRepository.save(wbr); System.out.print(word[0]);
            } else System.out.print('.');
        }
        return lines.size();
    }

    @RequestMapping("/stih")
    @ResponseBody
    private String stih(@RequestParam String url){return stih2base(url);}

    private final String root = "https://www.stihi.ru",
            localHost = "http://localhost:8080",
            linkClass=" class=[\"\'].+[\"\']",
            href = "<a href=(.+?)>";

    private static List<String> skiplst;

    //"http://www.stihi.ru/poems/list.html?type=selected" //
    //"http://www.stihi.ru/poems/list.html?topic=all"
    private final String editor2="https://www.stihi.ru/authors/",
            start = "https://www.stihi.ru/poems/list.html?topic=all",
            selected="https://www.stihi.ru/poems/selected.html",
            editor1="https://www.stihi.ru/authors/editor.html";

    @ResponseBody
    @RequestMapping("/links/{level}")
    private List<String> links (@PathVariable Long level, Model model) {
        skiplst=new ArrayList<>();
        List<String> poems=findAutorsPoems(editor1,level,"");
        if (poems.isEmpty())poems=findAutorsPoems(editor2,level,"");
        Integer c=poems.size();
        System.out.println("POEMS TO GO: "+c);
        for (String p:poems) System.out.println(--c+":"+!stih2base(p).isEmpty());
        return poems;
//        model.addAttribute("list", findPoems(url,2));
//        model.addAttribute("root", localHost+"/day/?url="+root );
//        return "mainIndex";
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
    private String mainPoems(@RequestParam(defaultValue=selected) String url,Model model) {

        List<String> poems = poems(url, root); // .subList(1,2);

        for (String l:mainPage(url,"")) // System.out.println(root+l);
            if (Pattern.compile("start").matcher(l).find())
//                System.out.println((root+l)+poems(root+l, root));
                poems.addAll(poems(root+l, root));

        Integer c=poems.size();
        System.out.println("POEMS TO GO: " + c);
        for (String p : poems)
            System.out.println(--c +": "+ p.replaceAll(".+/editor/","")+" :" + !stih2base(p).isEmpty());

        model.addAttribute("list", poems);
        return "poems";
    }

    @RequestMapping("/avtor")
    private String mainAuthors(@RequestParam(defaultValue=editor1) String url,Model model) {

        List<String> authors=new ArrayList<>();
        while (authors.isEmpty()) authors=authors(url,root); // localHost+"/avtor/?url="+root

//        List<String> list=new ArrayList<>();
        for (String a:authors) {
            System.out.println(a);
            List<String> poems=poems(a,root);
            Integer c=poems.size();
            System.out.println("POEMS TO GO: "+c);
            for (String p:poems)
                System.out.println(
                    a.replaceAll(".+/avtor/","")
                    +' '+--c+":"+!stih2base(p).isEmpty()
                );
    //        a.addAll(poems);
        }
        model.addAttribute("list",authors);
        return "avtors";
    }

    private List<String> mainPage(String url,String prefix) {
        List<String> ls = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        String s;
        while (m.find())
            if (Pattern.compile("month").matcher(m.group(1)).find())
                if (!ls.contains(s = Pattern.compile("\"").matcher(
                        Pattern.compile(linkClass).matcher(
                            m.group(1) // .replaceAll("&","%26")
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
                        Pattern.compile(linkClass).matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("%26") // "&amp;"
                ).replaceAll(""));
            else
                days.add(prefix+Pattern.compile("\"").matcher(
                    Pattern.compile("&").matcher(
                        Pattern.compile(linkClass).matcher(m.group(1)).replaceFirst("")
                    ).replaceAll("%26") // "&amp;"
                ).replaceAll(""));
        Collections.reverse(today);
//        Collections.sort(days);
//        today.addAll(days);
        return today;
    }

    private List<String> authors(String url,String prefix){
        List<String> authors = new ArrayList<>();
        String page=Pattern.compile("<div class=\"textlink\">.+?</div>").matcher(getPage(url)).replaceAll("");
        Matcher m = Pattern.compile(href).matcher(page);
//        System.out.println(page);
        while (m.find())
//         if(Pattern.compile("authorlink").matcher(m.group(1)).find())
            if (Pattern.compile("avtor").matcher(m.group(1)).find() ||
                Pattern.compile("/authors/editor").matcher(m.group(1)).find())
//                if (Pattern.compile("recomlink").matcher(m.group(1)).find())
                    authors.add(
                        Pattern.compile(prefix).matcher(m.group(1)).find()?"":prefix+
                            Pattern.compile("\"").matcher(
//                            Pattern.compile("&").matcher(
                                Pattern.compile(linkClass).matcher(
                                    m.group(1)
                                ).replaceAll("")
//                            ).replaceAll("%26") // "&amp;"
//                            ).replaceAll("&") // "&amp;"
                            ).replaceAll("")
                    );
//            else System.out.println(m.group(1));
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

    private String stih2base(String url){return stih2base(url,0b11);}

    private String stih2base(String url,int track){

        try {url = URLDecoder.decode(url,"UTF-8");}
        catch (UnsupportedEncodingException e) {e.printStackTrace();}

//        System.out.println(url);
        String poem=getPage(url);

        String title=localHost;
        Matcher tm = Pattern.compile("<h1>(.+?)</h1>").matcher(poem);
        if (tm.find()) title=tm.group(1);

        //System.out.println(poem);
        Matcher am = Pattern.compile("<div class=\"titleauthor\"><em><a href=\"(.+?)\">(.+?)</a></em></div>").matcher(poem);
        String authorName=am.find()?root+am.group(1):localHost;
        String realName=am.group(2); // am.find()?am.group(2):localHost;
//        System.out.println(realName);

//        poem=stripStih(poem);
        Matcher sm = Pattern.compile("<div class=\"text\">(.+?)</div>").matcher(poem);
        if (!sm.find()) return "";
//        System.out.println(m.group(1));
        poem = Pattern.compile("&nbsp;|&quot;").matcher(sm.group(1)).replaceAll(" ");
        poem = poem.replaceAll("\\s*<br>","\n");
        poem = poem.replaceAll("<.+>"," ");
        if(poem.isEmpty())return "";

//        System.out.println(poem);
        String[] lines=poem.split("\\n+");
        if (lines.length < 4 || lines.length > 99) return "";

        Article art = new Article(url);
        if (!articleRepository.findArticlesByName(art.getName()).isEmpty()) return "";

        Author author = authorRepository.findByName(authorName);
        if (author == null) authorRepository.save(author = new Author(authorName, realName));

        art.setAuthor(author);
        art.setTitle(title);
        articleRepository.save(art);

        System.out.print("#" + art.getId());
        System.out.print(' ' + url.replaceAll(root, ""));
        System.out.print(" vol:" + poem.length());
        System.out.print(" lines:" + lines.length);

        Long cn=art.getId()*1000L,wc = 0L;
        List<Text> text = new ArrayList<>();
        for (String inline:lines) {
//          System.out.println(line);
            String line=inline.trim();
            if (line.trim().length()>250) continue;
            Integer parts=line.replaceAll("[^аяёоуыиеэюАЯЁОУЫИЕЭЮaouieAOUIE]","").length();
            if ((parts<1)||(parts>30)) continue; // skip garbage texts
            String[] linewords = line.toLowerCase().replaceAll("[^а-яёa-z]", " ").trim().split("\\s+");
            if (linewords.length < 1||linewords.length > 250) continue;

            List<Clause> ex = clauseRepository.findClauseByClause(line);
//            if (ex.isEmpty())
            {
                Clause clause = new Clause(art, line, ++cn);
                Long cwc = 0L;
                Text clauseEnd = null;
                for (String word : linewords) // \\p{Alpha}
                    if (word.length() > 0)
                        try {
                            Wordbook wbr = wordbookRepository.findByWord(word);
                            if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word));
                            // clause.addWord(wbr, ++wc);
                            text.add(clauseEnd = new Text(art, clause, wbr, ++cwc));
                            wc++;
                        } catch (Exception e) {
                            System.out.println(e.getMessage() + " [" + word + "] ");
                        }
                if (clauseEnd != null) clause.setEnd(clauseEnd.setClause(true));
                clause.setWc(cwc);
                clauseRepository.save(clause);
            }
            /*
            else
            {
                System.out.println(" ("+cn+"):[ "+line+" ] ");
//                ex.forEach((c)->{System.out.print(c.getArticle().getId()+" : "+c.getClause());});
                Clause clause = new Clause(ex.get(0),art,cn);
                System.out.println(clause.getClause());
                clauseRepository.save(clause);
            }
            */
        }
        textRepository.saveAll(text);

        System.out.print(" clauses:" +cn%1000);
        art.setCc(cn%1000);
        System.out.print(" words:" +wc);
        art.setWc(wc);
        System.out.println();
        articleRepository.save(art);

        return poem;
    }

    private List<String> findAutorsPoems(String url,Long level,String left) {
//        System.out.println('['+url+']');
        skiplst.add(url);
        List<String> poems=poems(url,root);
        if (level>0)
            for (String lst:authors(url,root))
//            for (String lst:todayList(url,root))
//            for (String lst:mainPage(url,root))
                if (!skiplst.contains(lst))
                    poems.addAll(findAutorsPoems(lst,level-1,left+"+"));
        if (level>0)
            System.out.println(level+": "+left+url+" :"+poems.size());
        return poems;
    }

    private List<String> poems(String url,String prefix) {
//        System.out.println(url);
        List<String> poems = new ArrayList<>();
        Matcher m = Pattern.compile(href).matcher(getPage(url));
        while (m.find())
            if(Pattern.compile("poemlink").matcher(m.group(1)).find())
                poems.add(prefix+ // localHost + "/stih/?url=" +
                    Pattern.compile("\"").matcher(
                        Pattern.compile(linkClass).matcher(
                            m.group(1)
                        ).replaceFirst("")
                    ).replaceAll("")
                );
//        Collections.sort(poems);
        return poems;
    }

    private int ovalArrayNames (Article...names) {
        return names.length;
    }

}
