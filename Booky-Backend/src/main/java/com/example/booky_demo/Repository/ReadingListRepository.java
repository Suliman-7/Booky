package com.example.booky_demo.Repository;

import com.example.booky_demo.Model.ReadingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingListRepository extends JpaRepository<ReadingList, Integer> {

    ReadingList findReadingListById(int id);
}
