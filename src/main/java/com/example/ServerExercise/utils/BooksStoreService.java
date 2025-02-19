package com.example.ServerExercise.utils;

import com.example.ServerExercise.entities.BookMongo;
import com.example.ServerExercise.entities.BookPostgres;
import com.example.ServerExercise.model.book.Book;
import com.example.ServerExercise.model.book.Genre;
import com.example.ServerExercise.repositories.BookMongoRepository;
import com.example.ServerExercise.repositories.BookPostgresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BooksStoreService
{
    private int counterIdBooks;
    private final BookMongoRepository mongoRepository;
    private final BookPostgresRepository postgresRepository;

    @Autowired //
    public BooksStoreService(BookMongoRepository mongoRepository, BookPostgresRepository postgresRepository) {
        this.mongoRepository = mongoRepository;
        this.postgresRepository = postgresRepository;
        this.counterIdBooks = initializeCounter();
    }
    private int initializeCounter() {
       return mongoRepository.findAll().size() + 1;
    }
    public void checkAndCreateNewBook(Book book)
    {
        validateBook(book);
        book.setId(this.counterIdBooks++);
        mongoRepository.save(new BookMongo(book));
        postgresRepository.save(new BookPostgres(book));
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
        for(BookPostgres book:this.postgresRepository.findAll()) {
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
        return this.postgresRepository.findAll().size();
    }

    public  Set<Book> checkAndFilterBooks(Optional<String> author, Optional<Integer> priceBigger, Optional<Integer> priceLess,
                                   Optional<Integer> yearBigger, Optional<Integer> yearLess, Optional<String> genres,
                                          String persistenceMethod) {
        Set<Book> books = new HashSet<>();

        if (persistenceMethod.equals("MONGO")) {
            Set<BookMongo> setMongoBooks = this.mongoRepository.findAll().stream()
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

            // Convert BookMongo to Book and add to the books set
            for (BookMongo bookMongo : setMongoBooks) {
                books.add(bookMongo.createBookFromMongoBook());
            }
        } else {
            // Postgres query part
            Set<BookPostgres> setPostgresBooks = this.postgresRepository.findAll().stream()
                    .filter(book -> author.map(a -> book.getAuthor().equalsIgnoreCase(a)).orElse(true))
                    .filter(book -> priceBigger.map(price -> book.getPrice() >= price).orElse(true))
                    .filter(book -> priceLess.map(price -> book.getPrice() <= price).orElse(true))
                    .filter(book -> yearBigger.map(year -> book.getYear() >= year).orElse(true))
                    .filter(book -> yearLess.map(year -> book.getYear() <= year).orElse(true))
                    .filter(book -> genres.map(g -> {
                        String[] genreArray = g.split(",");
                        List<Genre> genreList = new ArrayList<>();
                        validateGenres(genreArray, genreList);
                        return genreList.stream().anyMatch(genre -> book.getGenres().contains(genre.name()));
                    }).orElse(true))
                    .collect(Collectors.toSet());

            for (BookPostgres bookPostgres  : setPostgresBooks) {
                books.add(bookPostgres.createBookFromPostgresBook());
            }
        }

        return books;
    }
    public Book IsBookExist(int id, String persistenceMethod)
    {
        Book result = null;
        if(persistenceMethod.equals("MONGO"))
        {
            BookMongo bookMongo = this.mongoRepository.findByRawid(id);
            if(bookMongo != null)
                result = bookMongo.createBookFromMongoBook();
        }
        else
        {
            BookPostgres bookPostgres = this.postgresRepository.findByRawid(id);
            if(bookPostgres != null)
                result = bookPostgres.createBookFromPostgresBook();
        }
        return result;
    }
    public BookMongo IsBookMongoExist(int id)
    {
        return this.mongoRepository.findByRawid(id);
    }
    public BookPostgres IsBookPostgresExist(int id)
    {
        return this.postgresRepository.findByRawid(id);
    }
    public int checkAndUpdatePrice(Integer id, Integer price)
    {
        BookMongo bookMongo = IsBookMongoExist(id);
        if(bookMongo == null)
            throw new IllegalStateException("Error: no such Book with id " + id);
        if(price <= 0)
            throw new IllegalArgumentException("Error: price update for book " + id + " must be a positive integer");
        int result = bookMongo.getPrice();

        BookPostgres bookPostgres = IsBookPostgresExist(id);
        bookMongo.setPrice(price);
        bookPostgres.setPrice(price);
        this.mongoRepository.save(bookMongo); // Persist to MongoDB
        this.postgresRepository.save(bookPostgres); // Persist to PostgreSQL
        return result;

    }
    public int checkAndDeleteBook(Integer id)
    {
        BookMongo bookMongo = IsBookMongoExist(id);

        if(bookMongo == null)
            throw new IllegalStateException("Error: no such Book with id " + id);
        BookPostgres bookPostgres = IsBookPostgresExist(id);
        this.mongoRepository.delete(bookMongo);
        this.postgresRepository.delete(bookPostgres);
        return this.postgresRepository.findAll().size();
    }
}
