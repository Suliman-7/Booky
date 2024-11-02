package com.example.booky_demo.Service;

import com.example.booky_demo.AuthorResponse;
import com.example.booky_demo.BookDetailsResponse;
import com.example.booky_demo.Repository.BookRepository;
import com.example.booky_demo.Model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final WebClient.Builder webClientBuilder;
    private final BookRepository bookRepository;



    public Mono<Book> fetchBookDetailsByISBN(String isbn) {
        String bookUrl = "https://openlibrary.org/isbn/" + isbn + ".json";

        return webClientBuilder.build()
                .get()
                .uri(bookUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> handleResponse(response, isbn))
                .flatMap(this::mapToBook);
    }

    private Mono<BookDetailsResponse> handleResponse(ClientResponse response, String isbn) {
        var statusCode = response.statusCode();
        var contentType = response.headers().contentType().orElse(MediaType.TEXT_HTML);

        if (statusCode.is3xxRedirection()) {
            String redirectedUrl = response.headers().header("Location").stream().findFirst().orElse(null);
            if (redirectedUrl != null) {
                return webClientBuilder.build()
                        .get()
                        .uri(redirectedUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(BookDetailsResponse.class)
                        .doOnError(error -> System.err.println("Error following redirect: " + error.getMessage()));
            } else {
                return Mono.error(new RuntimeException("No Location header found for redirect."));
            }
        } else if (statusCode.is2xxSuccessful() && contentType.equals(MediaType.APPLICATION_JSON)) {
            return response.bodyToMono(BookDetailsResponse.class);
        } else {
            return Mono.error(new UnsupportedOperationException("Unsupported response format or status for ISBN: " + isbn));
        }
    }

    private Mono<Book> mapToBook(BookDetailsResponse bookDetails) {
        if (bookDetails.getAuthors() != null && bookDetails.getAuthors().length > 0) {
            String authorKey = bookDetails.getAuthors()[0].getKey();
            fetchAuthorDetails(authorKey)
                    .map(authorName -> {

                        Book book = new Book();
                        book.setAuthor(authorName);
                        book.setTitle(bookDetails.getTitle());
                        book.setCoverImageUrl(bookDetails.getCoverImageUrl());
                        book.setNumberOfPages(bookDetails.getNumberOfPages());

                        bookRepository.save(book);

                        return book;
                    })
                    .subscribe();


            return fetchAuthorDetails(authorKey)
                    .map(authorName -> new Book(
                            null,
                            bookDetails.getTitle(),
                            authorName,
                            bookDetails.getCoverImageUrl(),
                            bookDetails.getNumberOfPages(),
                            null
                    ));
        } else {
            return Mono.just(new Book(
                    null,
                    bookDetails.getTitle(),
                    null,
                    bookDetails.getCoverImageUrl(),
                    bookDetails.getNumberOfPages(),
                    null
            ));
        }
    }

    private Mono<String> fetchAuthorDetails(String authorKey) {
        String authorUrl = "https://openlibrary.org/authors/" + authorKey.replace("/authors/", "") + ".json";

        return webClientBuilder.build()
                .get()
                .uri(authorUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AuthorResponse.class)
                .map(AuthorResponse::getName);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
