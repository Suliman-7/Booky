package com.example.booky_demo.Service;

import com.example.booky_demo.Api.ApiException;
import com.example.booky_demo.Model.Book;
import com.example.booky_demo.Model.ReadingList;
import com.example.booky_demo.Repository.BookRepository;
import com.example.booky_demo.Repository.ReadingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReadingListService {

    private final ReadingListRepository readingListRepository;
    private final BookRepository bookRepository;

    public List<ReadingList> getAllReadingLists() {
        return readingListRepository.findAll();
    }

    public void addReadingList(String name) {
        ReadingList readingList = new ReadingList();
        readingList.setName(name);
        readingListRepository.save(readingList);
    }

    public void addBookToReadingList(Integer bookId, Integer readingListId) {
        ReadingList readingList = readingListRepository.findReadingListById(readingListId);
        if (readingList == null) {
            throw new ApiException("readingList not found");
        }
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            throw new ApiException("book not found");
        }
        readingList.getBooks().add(book);
        book.setReadingList(readingList);
        bookRepository.save(book);
        readingListRepository.save(readingList);
    }

    public List<Book> getBooksbyReadingListId(Integer readingListId) {
        ReadingList readingList = readingListRepository.findReadingListById(readingListId);
        List<Book> readingListBooks = new ArrayList<>();
        System.out.println(readingList.getName());
        if (readingList == null) {
            throw new ApiException("readingList not found");
        }
        for (Book book : readingList.getBooks()) {
            readingListBooks.add(book);
        }
        return readingListBooks;
    }


}
