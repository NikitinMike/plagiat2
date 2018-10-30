package mikenikitin.plagiat2;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Article;
import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.ArticleRepository;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Plagiat2Application implements CommandLineRunner{

	public static void main(String[] args) throws IOException {
		String text = "Hello world";
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(new File("example.txt")));
			output.write(text);
		} catch ( IOException e ) { e.printStackTrace();
		} finally { if ( output != null ) output.close();}
		SpringApplication.run(Plagiat2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
