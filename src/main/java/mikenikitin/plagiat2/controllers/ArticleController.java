package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Text;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.ClauseRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping({"article","articles"})
public class ArticleController {

    private ClauseRepository clauseRepository;

    private ArticleRepository articleRepository;
//    protected List<Article> articles = articleRepository.findAll();

    static private String lastOrder="id";
    static private boolean reverse=true;
    @RequestMapping({"/","/order/"}) // ,"/page"
    private String index(Model model,
        @SortDefault.SortDefaults({
//            @SortDefault(sort="article", direction=Sort.Direction.ASC),@SortDefault(sort="number", direction=Sort.Direction.ASC)
        }) @PageableDefault(size = 100) Pageable pageable
    ) {
        List<Article> articles = articleRepository.findAll();
        switch (lastOrder) {
            case "name":
                Collections.sort(articles,(b,a)->(a.getName().compareTo(b.getName())));
                break;
            case "wc":
                Collections.sort(articles,(b,a)->(a.getWc().intValue()-b.getWc().intValue()));
                break;
            case "cc":
                Collections.sort(articles,(b,a)->(a.getCc().intValue()-b.getCc().intValue()));
                break;
            case "title":
                Collections.sort(articles,(b,a)->(a.getTitle().compareTo(b.getTitle())));
                break;
            case "author":
                Collections.sort(articles,(b,a)->(a.getAuthor().getRealname().compareTo(b.getAuthor().getRealname())));
                break;
            case "id":break;
            default:break;
        }
        if (reverse) Collections.reverse(articles);
//        List<Integer> pages=new ArrayList<Integer>(){{for (int i = 0; i <= articles.size()/pageable.getPageSize(); i++) add(i); }};
//        model.addAttribute("pages",pages);
        model.addAttribute("pages",articles.size()/pageable.getPageSize());
        model.addAttribute("articles", articles);
//        model.addAttribute("articles", articleRepository.findAll(pageable));
        return "indexArticles";
    }

    @RequestMapping({"/page"})
//    @ResponseBody
//    private Page<Clause> pageable(Model model,
    private String page(Model model,
        @SortDefault.SortDefaults({
//            @SortDefault(sort = "article", direction = Sort.Direction.ASC),@SortDefault(sort = "number", direction = Sort.Direction.ASC)
        }) @PageableDefault(size = 99) Pageable pageable)
    {
//        int pagesCount=articleRepository.findAll().size()/pageable.getPageSize();
//        List<Integer> pages=new ArrayList<Integer>() {{for (int i = 0; i <= pagesCount; i++) add(i); }};
        model.addAttribute("pages",articleRepository.count()/pageable.getPageSize());
        model.addAttribute("articles", articleRepository.findAll(pageable));
        // .getContent() pageable = updatePageable(pageable,999)
        return "indexArticles";
    }

    @RequestMapping({"/order/{order}"}) // name wc title author
    private String orderBy(@PathVariable String order, Model model) {
        if(!order.isEmpty()) if (order.equals(lastOrder)) reverse=!reverse; else lastOrder=order;
        return "redirect:/articles/";
    }

//    @RequestMapping
//    @ResponseBody
//    private List<Article> listall(){return articleRepository.findAll();}

    //    @RequestMapping("{id}")
    @ResponseBody
    private List<String> article(@PathVariable Long id, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        List<String> list = new ArrayList<>(); // = null;
        for (Text t:articleRepository.findArticlesById(id).getText()) list.add(t.getWord().getWord());
        // System.out.print(t.getWord().getWord());
        // .forEach((b) -> System.out.println(b))
//        articleRepository.findArticlesById(id).getText().forEach((t)->System.out.print(t.getWord().getWord()+" "));
//        articleRepository.findArticlesById(id).getText().map((t)->(t.getWord().getWord()));
//        articleRepository.findArticlesById(id).getText().forEach((t)->(t.getWord().getWord()));
        return list;
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

    @RequestMapping("/delete/{id}")
    private String delete(HttpServletRequest request, @PathVariable Long id, HttpServletResponse response){
        Article art=articleRepository.findArticlesById(id);
        if(art==null) return "redirect:/articles/";
        articleRepository.delete(art);
//        clauseRepository.findClausesByArticle_Id(art.getId()).forEach((t)->(deleteById(t.getId())));
//        return "redirect:/articles/";
        return "redirect:"+ request.getHeader("Referer");
//        return getPreviousPageByRequest(request).orElse("/"); //else go to home page
    }


    @RequestMapping("{id}")
    private String article2(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        Article art=articleRepository.findArticlesById(id);
        model.addAttribute("id",id);
        model.addAttribute("origin", art.getName());
        model.addAttribute("author",art.getAuthor());
        model.addAttribute("title",art.getTitle());
        model.addAttribute("article", art.getText());
        return "article";
    }

//    @ResponseBody
    @RequestMapping("/table/{id}")
    private String article3(HttpServletRequest request, @PathVariable Long id , Model model, HttpServletResponse response) throws IOException {
//        if (id==null) return "redirect:"+ request.getHeader("Referer");
        Article art=articleRepository.findArticlesById(id);
//        if (art==null) return "redirect:"+ request.getHeader("Referer");
        if (art == null) return "redirect:/";
        model.addAttribute("id",id);
        model.addAttribute("author",art.getAuthor());
        model.addAttribute("title",art.getTitle());
        model.addAttribute("table",clauseRepository.findClausesByArticle_Id(id));
        return "articleTable";
    }

    @RequestMapping("/flat/{id}")
    private String article(@PathVariable Long id, Model model, HttpServletResponse response) throws IOException {
        if(articleRepository.findArticlesById(id)==null){response.sendRedirect("/article");return null;}
        model.addAttribute("wordbook", articleRepository.findArticlesById(id).getText());
        return "WordBook";
    }

}
