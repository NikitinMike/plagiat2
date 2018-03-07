package mikenikitin.plagiat2.services;

import org.springframework.stereotype.Service;

@Service
public class ArticleService {


/*
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

//        stih=stripStih(stih);
        Matcher sm = Pattern.compile("<div class=\"text\">(.+?)</div>").matcher(stih);
        if (!sm.find()) return "";
//        System.out.println(m.group(1));
//        return Pattern.compile("&nbsp;|&quot;").matcher(m.group(1).replaceAll("<br>","\n")).replaceAll(" ");
        stih = Pattern.compile("&nbsp;|&quot;").matcher(sm.group(1)).replaceAll(" ").replaceAll("<br>","\n");
        if(stih.isEmpty())return "";

        String[] lines=stih.replaceAll("[^а-яёА-ЯЁ\n]"," ").split("\\n+");
        if(lines.length<33||lines.length>555) return "";
//        System.out.println(words.toString());
        for (String line:lines) // \\p{Alpha}
            if (line.length()>0)
                System.out.println(line);
        if(1==1) return "";

        Article art= new Article(url);
        if (articleRepository.findArticlesByName(art.getName())!=null) return "";

        Author author=authorRepository.findByName(authorName);
        if (author==null) authorRepository.save(author=new Author(authorName,realName));

        art.setAuthor(author);
        art.setTitle(title);

        Long wc=0L;
        List<Text> text = new ArrayList<>();
        articleRepository.save(art);

        System.out.print("#"+art.getId());
        System.out.print(' '+url.replaceAll("http://www.stihi.ru/",""));
        System.out.print(" length:"+stih.length());
//        System.out.println(" words:"+words.length);

//        List<String> nw=new ArrayList<>(); // new words in wordbook
        for (String line:lines) // \\p{Alpha}
            if (line.length()>0)
        for (String word:line.split("\\s+")) // \\p{Alpha}
            if (word.length()>0) {
//                System.out.print(word+",");
                Wordbook wbr=wordbookRepository.findByWord(word.toLowerCase());
//                if (wbr == null) System.out.print(".");
//                if (wbr == null) nw.add(word.toLowerCase());
//                if (wbr == null) System.out.print(' '+word.toLowerCase());
                if (wbr == null) wordbookRepository.save(wbr = new Wordbook(word.toLowerCase()));
                text.add(new Text(art,wbr,++wc));
            }
//        System.out.println(' ');
//        System.out.println(" WC:"+wc);
//        System.out.println(nw);

        textRepository.saveAll(text);
        art.setWc(wc);
        articleRepository.save(art);
        authorRepository.save(author);

        return stih;
//        return url;
    }

*/

}
