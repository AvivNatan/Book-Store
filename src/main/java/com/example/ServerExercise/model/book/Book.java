package com.example.ServerExercise.model.book;

import com.example.ServerExercise.entities.BookMongo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Book {
    private int id;
    private String title;
    private String author;
    private int price;
    private int year;
    private List<Genre> genres;

    public Book(String title, String author, int price, int year, List<Genre> genres) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.year = year;
        this.genres = genres;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public List<Genre> getGenres() {
        return genres;
    }
    public boolean IsYearInLimit()
    {
        return this.year <= 2100 && this.year>= 1940;
    }
    // Convert from a comma-separated string to a List<Genre>
}
