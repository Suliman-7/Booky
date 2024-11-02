package com.example.booky_demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorResponse {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

}
