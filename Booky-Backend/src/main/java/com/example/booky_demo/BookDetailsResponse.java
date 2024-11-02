package com.example.booky_demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDetailsResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("number_of_pages")
    private int numberOfPages;

    @JsonProperty("covers")
    private int[] covers;

    @JsonProperty("authors")
    private Author[] authors;

    public String getTitle() {
        return title;
    }


    public int getNumberOfPages() {
        return numberOfPages;
    }




    public Author[] getAuthors() {
        return authors;
    }


    public String getCoverImageUrl() {
        if (covers != null && covers.length > 0) {
            return "https://covers.openlibrary.org/b/id/" + covers[0] + "-L.jpg";
        }
        return null;
    }
}
