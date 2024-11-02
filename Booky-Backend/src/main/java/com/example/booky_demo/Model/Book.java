package com.example.booky_demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor


public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(100) not null")
    private String title;

    @Column(columnDefinition = "varchar(100) not null")
    private String author;

    @Column(columnDefinition = "varchar(100) not null")
    private String coverImageUrl;

    @Column(columnDefinition = "varchar(100) not null")
    private int numberOfPages;


    @ManyToOne
    @JsonIgnore
    private ReadingList readingList;



}
