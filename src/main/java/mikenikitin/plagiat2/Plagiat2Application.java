package mikenikitin.plagiat2;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Plagiat2Application implements CommandLineRunner{

	ArticleRepository articleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Plagiat2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://ru.wikipedia.org/wiki/%D0%A1%D0%BB%D1%83%D0%B6%D0%B5%D0%B1%D0%BD%D0%B0%D1%8F:%D0%A1%D0%BB%D1%83%D1%87%D0%B0%D0%B9%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        for(String line;(line=rd.readLine())!=null;) result.append(line); rd.close();
        Long wc=0L;

        for (String w:result.toString().replaceAll("[^а-яёА-ЯЁ]"," ").split("\\s+")) // \\p{Alpha}
            if (w.length()>2) System.out.print(++wc+":"+w.toLowerCase()+" ");
        System.out.println("");

//        a.setWc(wc); // System.out.println(wc);
        articleRepository.save(new Article(URLDecoder.decode(conn.getURL().toString(),"UTF-8"),wc));
//        System.out.println("Article:"+ a);

//        List<Article> articles = new ArrayList<>(); // articles.add(a);
        List<Article> articles = articleRepository.findAll();
//        articleRepository.findAll().forEach((b) -> articles.add(b));
        System.out.println("ArticlesList:"+ articles);
//        for (Article article: articleRepository.findAll()) System.out.println(article);
//        articleRepository.findAll().forEach((b) -> System.out.println(b));
//        articleRepository.saveAll(articles);

	}
}
