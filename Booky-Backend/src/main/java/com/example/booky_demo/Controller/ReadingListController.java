package com.example.booky_demo.Controller;


import com.example.booky_demo.Service.ReadingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readingList")
@RequiredArgsConstructor


public class ReadingListController {

    private final ReadingListService readingListService;

    @GetMapping("get-all-reading-lists")
    public ResponseEntity getAllReadingLists() {
        return ResponseEntity.status(200).body(readingListService.getAllReadingLists());
    }

    @PostMapping("add-reading-list/{name}")
    public ResponseEntity getAllReadingLists(@PathVariable String name) {
        readingListService.addReadingList(name);
        return ResponseEntity.status(200).body("Successfully added reading list");
    }

    @PostMapping("add-book-to-reading-list/{bid}/{rlid}")
    public ResponseEntity getAllReadingLists(@PathVariable Integer bid, @PathVariable Integer rlid) {
        readingListService.addBookToReadingList(bid,rlid);
        return ResponseEntity.status(200).body("Successfully book added to reading list");
    }

    @GetMapping("/get-reading-list-books/{rlid}")
    public ResponseEntity getReadingListBooks(@PathVariable Integer rlid) {
        return ResponseEntity.status(200).body(readingListService.getBooksbyReadingListId(rlid));

    }





}
