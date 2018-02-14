package mikenikitin.plagiat2.controllers;

import lombok.AllArgsConstructor;
import mikenikitin.plagiat2.model.Book;
import mikenikitin.plagiat2.repository.BookRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    BookRepository bookRepository;


    @RequestMapping("/{id}")
    private List<Book> hello(@PathVariable Integer id){
        return bookRepository.findBooksByIssue(id);
    }
}
