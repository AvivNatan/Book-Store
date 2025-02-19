package com.example.ServerExercise.entities;
import com.example.ServerExercise.model.book.Book;
import com.example.ServerExercise.model.book.Genre;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

// this is Jsons view //
@Document(collection = "books")
public class BookMongo {

    // in mongo this is default primary key - mongo treat this field always as primary key
    // we can not change the primary key to rawid
    // it will cause problems because will be 2 primary keys
    @Id // this is primary key
    private String _id;
    private Integer rawid;
    private String title;
    private String author;
    private Integer year;
    private Integer price;
    private List<Genre> genres; // here the list is ok

    public BookMongo() {
    }

    public BookMongo(Book book) {
        this.rawid =  book.getId();
        this.title =  book.getTitle();
        this.author =  book.getAuthor();
        this.year =  book.getYear();
        this.price = book.getPrice();
        this.genres =   book.getGenres();
    }
    public Book createBookFromMongoBook()
    {
        Book bookResult = new Book(this.title, this.author,this.price,
                this.year, this.genres);
        bookResult.setId(this.rawid);
        return bookResult;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
