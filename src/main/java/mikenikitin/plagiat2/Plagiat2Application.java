package mikenikitin.plagiat2;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Book;
import mikenikitin.plagiat2.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Plagiat2Application implements CommandLineRunner{

	BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(Plagiat2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		List<Book> books = new ArrayList<>();
//
//		for (int i = 0; i <10 ; i++) {
//			books.add(new Book("test"+i, i));
//		}
//		bookRepository.saveAll(books);

	}
}
