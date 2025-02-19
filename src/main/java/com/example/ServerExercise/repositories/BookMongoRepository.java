package com.example.ServerExercise.repositories;

import com.example.ServerExercise.entities.BookMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookMongoRepository extends MongoRepository<BookMongo, Integer> {
    BookMongo findByRawid(Integer rawid);
}
