package com.example.ServerExercise.repositories;

import com.example.ServerExercise.entities.BookMongo;
import com.example.ServerExercise.entities.BookPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPostgresRepository extends JpaRepository<BookPostgres, Integer>
{
    BookPostgres findByRawid(Integer rawid);
}
