package mikenikitin.plagiat2.controllers;

//import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.TextRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;

@Controller
//@AllArgsConstructor
@RequestMapping("wordbook")
public class WordbookController {

    private WordbookRepository wordbookRepository;
    private TextRepository textRepository;
    private ArticleRepository articleRepository;

//    @RequestMapping("/rest")
//    @ResponseBody
//private String listAllByEnds(Model model){ // Map<String, List<Wordbook>>
//    private Map<String, String> listall(){
//        Set uniqueValues = new HashSet(wordbookRepository.findAll().forEach()); //now unique
//        List<Wordbook> wb= wordbookRepository.findAll();
//        wb.forEach(Wordbook::getWord);

//        BinaryOperator<String> binaryOpt = (s1, s2)->s1+" "+s2;
//        return new HashSet(wordbookRepository.findAll().stream()
//    Map<String, List<Wordbook>> wb=wordbookRepository.findAll().stream()
//                .map(Wordbook::getEnd)
//                .map(Wordbook::getWord)
//            .filter(a->a.getWord().replaceAll("[а-яёА-ЯЁ]","").isEmpty())
//                .limit(10000L)
//                .distinct()
//                .sorted((b,a)->a.getEnd().compareTo(b.getEnd()))
//                .sorted()
//                .collect(Collectors.toMap(Function.identity(), data->data)); // ,(getEnd1, getEnd2) -> getEnd1+";"+getEnd2)
//                .collect(Collectors.toMap(p->p,p->p,(x,y)->x+", "+y));
//                .collect(Collectors.toMap(Wordbook::getEnd,Wordbook::getWord,(word1,word2)->word1+" "+word2)); // binaryOpt
//            .collect(Collectors.groupingBy(Wordbook::getEnd)); // Wordbook::getEnd,Wordbook::getWord
//                .collect(Collectors.toList());
//                .collect(Collectors.toSet());
//                .forEach();
//                .getContent()

    String revstr(String s){return new StringBuilder(s).reverse().toString();}

    @RequestMapping("/ends")
    private String listAllByEnds(Model model){
        List<Map.Entry<String, List<Wordbook>>> list = new LinkedList(
            wordbookRepository.findAll().stream()
                .filter(a->a.getWord().replaceAll("[а-яёА-ЯЁ]","").isEmpty())
                .sorted((b,a)->a.getWord(true).compareTo(b.getWord(true)))
                .collect(Collectors.groupingBy(Wordbook::getEnd)).entrySet()
        );
//        Collections.sort(list,(o1,o2)->o2.getValue().size()-o1.getValue().size());
        Collections.sort(list,(o1,o2)->revstr(o1.getKey()).compareTo(revstr(o2.getKey())));
        Map<String, List<Wordbook>> rythm = new LinkedHashMap();
        for (Map.Entry<String, List<Wordbook>> item : list) rythm.put(item.getKey(), item.getValue());
//            item.getValue().sort((a, b)->(a.getWord(true).compareTo(b.getWord(true))));
        model.addAttribute("wordbook",rythm);
//        model.addAttribute("wordbook", sortByValue(wb) );
        return "WordBookEnds2";
    }

    /*
    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {
        List<Map.Entry<K,V>> list =new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());
        Collections.sort(list,(o1,o2)->o2.getValue().size()-o1.getValue().size());
        Map<K,V> result = new LinkedHashMap<K,V>();
        for (Map.Entry<K,V> entry:list) result.put(entry.getKey(), entry.getValue());
        return result;
    }
    */

    @RequestMapping({"/index", "/"})
    private String index(Model model,Pageable pageable) {
        List<Wordbook> wb= wordbookRepository.findAll();
//        System.out.println(wb);
        sort(wb,(a, b)->(a.getWord(true).compareTo(b.getWord(true))));
//        Collections.sort(wb,(a, b)->(a.getLetters().compareTo(b.getLetters())));
        reverse(wb);
        model.addAttribute("wordbook", wb); // .getContent()
        return "WordBook";
    }

    @RequestMapping("/order")
    private String order(Model model) {
        List<Wordbook> wb=wordbookRepository.findAll();
        wb.sort((a,b)->(a.getWord().compareTo(b.getWord())));
//        Collections.sort(wb,(a,b)->(a.getWord().compareTo(b.getWord())));
        model.addAttribute("wordbook", wb);
        return "WordBook2";
    }

    /*
        @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
        @SortDefault.SortDefaults({
                @SortDefault(sort = "dateRecorded", direction = Sort.Direction.DESC),
                @SortDefault(sort = "encounterId", direction = Sort.Direction.ASC)
        })
    */
    @RequestMapping({"/page"})
    private String pageable( Model model,
        @SortDefault.SortDefaults(@SortDefault(sort = "word", direction = Sort.Direction.ASC))
        @PageableDefault(size = 1000/2) Pageable pageable)
    {
//        int pagesCount=wordbookRepository.count()/pageable.getPageSize();
//        List<Integer> pages=new ArrayList<Integer>() {{for (int i = 0; i <= pagesCount; i++) add(i); }};
        model.addAttribute("pages",wordbookRepository.count()/pageable.getPageSize());
        model.addAttribute("wordbook", wordbookRepository.findAll(pageable)); // .getContent() pageable = updatePageable(pageable,999)
        return "WordBook";
    }

    @RequestMapping("/word/{id}")
    @ResponseBody
    private String id(@PathVariable Long id,Model model) {
//    private Wordbook id(@PathVariable Long id,Model model) {
        Wordbook word=wordbookRepository.getById(id);
        System.out.println(word.getParts());
        return word.getParts();
/*
        List<Article> articles=new ArrayList<>();
        for (Text txt:textRepository.getAllByWord(wordbookRepository.getById(id)))
//            System.out.println(txt.getArticle().getTitle());

//            if (!articles.contains(txt.getArticle()))
//                if (articleRepository.existsById(txt.getArticle().getId()))
//                    articles.add(txt.getArticle());

        model.addAttribute("articles", articles);
        return "indexArticles";
//        return texts.stream().map(text -> text.getArticle()).collect(Collectors.toList());
*/
    }

    @RequestMapping("/{sym}")
    private String symbol(Model model,@PathVariable String sym) {
        List<Wordbook> wb=wordbookRepository.findAllByWordLike(sym+'%');
        wb.sort((a,b)->(a.getWord().compareTo(b.getWord())));
        model.addAttribute("wordbook",wb);
        return "WordBook";
    }

    @RequestMapping("/fill")
    @ResponseBody
    private void fillWB(){
        List<Wordbook> wb=wordbookRepository.findAll();
        wb.forEach(a->a.setSize(a.getLetters(false).length()));

//        wb.forEach(System.out::println);
//        wb.forEach(Wordbook::getWord);

//        List<String> a = Arrays.asList("123","234","345");
//        List<Integer> b = a.stream()
//                .map(String::length)
//                .collect(Collectors.toList());

        wordbookRepository.saveAll(wb);
    }

    @RequestMapping("/set")
    @ResponseBody
    private void set(){
        Wordbook wb=new Wordbook("");
//        wb.getSet().forEach((s)-> System.out.print(s+" "));
    }

}
