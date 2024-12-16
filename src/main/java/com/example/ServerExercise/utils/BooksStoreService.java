package com.example.ServerExercise.utils;

import com.example.ServerExercise.model.book.Book;
import com.example.ServerExercise.model.book.Genre;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

public class BooksStoreService
{
    private int counterIdBooks;
    private final List<Book> allBooks;

    public BooksStoreService()
    {
        this.counterIdBooks = 0;
        this.allBooks = new ArrayList<>();
    }
    public void checkAndCreateNewBook(Book book)
    {
        validateBook(book);
        book.setId(++this.counterIdBooks);
        this.allBooks.add(book);
    }
    private void validateBook(Book book)
    {
        if(checkTitleExist((book.getTitle())))
            throw new IllegalArgumentException("Error: Book with the title ["+ book.getTitle() + "] already exists in the system");
        if(!book.IsYearInLimit())
            throw new IllegalArgumentException("Error: Can’t create new Book that its year " + book.getYear() + " is not in the accepted range [1940 -> 2100]");
        if(book.getPrice()<=0)
            throw new IllegalArgumentException("Error: Can’t create new Book with negative price");
        //validation ok

    }

    public int getCounterIdBooks() {
        return counterIdBooks;
    }
    private boolean checkTitleExist(String title)
    {
        for(Book book:this.allBooks) {
            if (book.getTitle().equalsIgnoreCase(title))
                return true;
        }
        return false;
    }
    private void validateGenres(String[] genresString, List<Genre> genres)
    {
        for(String genre: genresString)
        {
            try {
                //the try for the convert from string to Genre
                genres.add(Genre.valueOf(genre));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException();
            }
        }

    }
    public int getTotalNumberBooks()
    {
        return this.allBooks.size();
    }
    public  Set<Book> checkAndFilterBooks(Optional<String> author, Optional<Integer> priceBigger, Optional<Integer> priceLess,
                                   Optional<Integer> yearBigger, Optional<Integer> yearLess, Optional<String> genres) {

        return this.allBooks.stream()
                .filter(book -> author.map(a -> book.getAuthor().equalsIgnoreCase(a)).orElse(true))
                .filter(book -> priceBigger.map(price -> book.getPrice() >= price).orElse(true))
                .filter(book -> priceLess.map(price -> book.getPrice() <= price).orElse(true))
                .filter(book -> yearBigger.map(year -> book.getYear() >= year).orElse(true))
                .filter(book -> yearLess.map(year -> book.getYear() <= year).orElse(true))
                .filter(book -> genres.map(g -> {
                    String[] genreArray = g.split(",");
                    List<Genre> genreList = new ArrayList<>();
                    validateGenres(genreArray, genreList);
                    return genreList.stream().anyMatch(book.getGenres()::contains);
                }).orElse(true))
                .collect(Collectors.toSet());
    }
    public Book IsBookExist(int id)
    {
       for(Book book:this.allBooks)
           if(book.getId()==id)
               return book;
       return null;
    }
    public int checkAndUpdatePrice(Integer id, Integer price)
    {
        Book book = IsBookExist(id);
        if(book == null)
            throw new IllegalStateException("Error: no such Book with id " + id);
        if(price <= 0)
            throw new IllegalArgumentException("Error: price update for book " + id + " must be a positive integer");
        int result = book.getPrice();
        book.setPrice(price);
        
        return result;

    }
    public int checkAndDeleteBook(Integer id)
    {
        Book book = IsBookExist(id);
        if(book == null)
            throw new IllegalStateException("Error: no such Book with id " + id);
        this.allBooks.remove(book);
        return this.allBooks.size();
    }
}
