package com.example.ServerExercise.entities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ServerExercise.model.book.Book;
import com.example.ServerExercise.model.book.Genre;
import jakarta.persistence.*;

// this is Table view //

@Entity
@Table(name = "books")
public class BookPostgres {
    @Id
    @Column(name = "rawid")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rawid;
    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "year")
    private Integer year;

    @Column(name = "price")
    private Integer price;

    @Column(name = "genres")
    private String genres;

    public BookPostgres(){}
    public BookPostgres(Book book) {
        this.rawid = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.price = book.getPrice();
        setGenresList(book.getGenres());
    }
    public Book createBookFromPostgresBook()
    {
        List<Genre> genres = getGenresList();
        Book bookResult = new Book(this.title, this.author,this.price,
                this.year, genres);
        bookResult.setId(this.rawid);
        return bookResult;
    }
    // Getters and setters
    //getGenresList: Converts the string into
    // a list of genres when you need to work
    // with them as a List<Genre> in your application.
    public List<Genre> getGenresList() {
        if (genres == null || genres.isEmpty()) {
            return new ArrayList<>();
        }
        // Remove brackets and spaces, then split by comma
        return Arrays.stream(genres.replaceAll("[\\[\\]\\s]", "").split(","))
                .map(genre -> genre.replace("\"", "")) // Remove quotes from each genre
                .map(Genre::valueOf) // Convert to Genre enum
                .toList();
    }
    //setGenresList: Takes a List<String> of genres and converts
    // it into a comma-separated string for storage.
    public void setGenresList(List<Genre> genresList) {
        this.genres = "[" + genresList.stream()
                .map(genre -> "\"" + genre.name() + "\"") // Add quotes around each genre name
                .collect(Collectors.joining(",")) + "]"; // Join with commas and wrap in brackets
    }
    public Integer getRawid() {
        return rawid;
    }

    public void setRawid(Integer rawid) {
        this.rawid = rawid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
