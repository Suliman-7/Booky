package com.example.booky_demo.Controller;

import com.example.booky_demo.Model.Book;
import com.example.booky_demo.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor

public class BookController {

    private final BookService bookService;


    @PostMapping("/books/{isbn}")
    public Mono<Book> getBookDetails(@PathVariable String isbn) {
        return bookService.fetchBookDetailsByISBN(isbn);
    }
    @GetMapping("/get-all-books")
    public ResponseEntity getAllBooks() {
        return ResponseEntity.status(200).body(bookService.getAllBooks());
    }
}
