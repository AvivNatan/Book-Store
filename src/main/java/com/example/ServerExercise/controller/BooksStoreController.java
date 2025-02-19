package com.example.ServerExercise.controller;
import com.example.ServerExercise.loggim.LoggerHelper;
import com.example.ServerExercise.model.book.Book;
import com.example.ServerExercise.model.responses.ServerResponse;
import com.example.ServerExercise.utils.BooksStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BooksStoreController
{
    private final BooksStoreService bookService;
    private final LoggerHelper loggerHelper = new LoggerHelper();
    // Constructor injection for BooksStoreService
    @Autowired
    public BooksStoreController(BooksStoreService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/books/health")
    public String Health() {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/books/health","GET");
        loggerHelper.addDebugLogForLogRequests();
        return "OK";
    }
    @PostMapping(value = "/book", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> createNewBook(@RequestBody Book book)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/book","POST");
        int booksNumberWithoutCurrent =  this.bookService.getTotalNumberBooks();

        try {
            this.bookService.checkAndCreateNewBook(book);
            ServerResponse response = new ServerResponse(book.getId());
            this.loggerHelper.addLogsToCreateBookEndPoint(book.getTitle(),booksNumberWithoutCurrent,book.getId());

            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ServerResponse response = new ServerResponse(ex.getMessage());
            this.loggerHelper.addLogError(ex.getMessage());
            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

    }
    @GetMapping(value = "/books/total", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> getTotalBooks(@RequestParam(required = false) String author,
                                                        @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                        @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                        @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                        @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                        @RequestParam(required = false) String genres,
                                                        @RequestParam String persistenceMethod)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/books/total","GET");

        try {
            int numberFilteredBooks = this.bookService.checkAndFilterBooks(
                    Optional.ofNullable(author),
                    Optional.ofNullable(priceBiggerThan),
                    Optional.ofNullable(priceLessThan),
                    Optional.ofNullable(yearBiggerThan),
                    Optional.ofNullable(yearLessThan),
                    Optional.ofNullable(genres),persistenceMethod)
                    .size();
            ServerResponse response = new ServerResponse(numberFilteredBooks);
            this.loggerHelper.addLogToBooksCountAndDetailsEndPoint(numberFilteredBooks);
            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ServerResponse response = new ServerResponse();
            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> getBooksData(@RequestParam(required = false) String author,
                                                        @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                        @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                        @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                        @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                        @RequestParam(required = false) String genres,
                                                        @RequestParam String persistenceMethod)

    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/books","GET");

        try {
             Set<Book> result =this.bookService.checkAndFilterBooks(
                            Optional.ofNullable(author),
                            Optional.ofNullable(priceBiggerThan),
                            Optional.ofNullable(priceLessThan),
                            Optional.ofNullable(yearBiggerThan),
                            Optional.ofNullable(yearLessThan),
                            Optional.ofNullable(genres), persistenceMethod);
            // Convert the set to a sorted set using TreeSet
            SortedSet<Book> sortedResult = new TreeSet<>(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
            sortedResult.addAll(result);

            ServerResponse response = new ServerResponse(sortedResult);
            this.loggerHelper.addLogToBooksCountAndDetailsEndPoint(sortedResult.size());
            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ServerResponse response = new ServerResponse();
            this.loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/book", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> getSingleBookData(@RequestParam Integer id,
                                                            @RequestParam String persistenceMethod)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/book","GET");

        Book book = bookService.IsBookExist(id,persistenceMethod);
        if(book==null) {
            loggerHelper.addDebugLogForLogRequests();
            ServerResponse serverResponse = new ServerResponse("Error: no such Book with id " + id);
            return new ResponseEntity<>(serverResponse, HttpStatus.BAD_REQUEST);
        }
        ServerResponse serverResponse = new ServerResponse(book);
        this.loggerHelper.addLogToBookDetailsEndPoint(book.getId());
        loggerHelper.addDebugLogForLogRequests();
        return new ResponseEntity<>(serverResponse, HttpStatus.OK);
    }
    @PutMapping(value = "/book", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> updateBookPrice(@RequestParam Integer id, @RequestParam Integer price)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/book","PUT");

        try {
            int oldPrice = this.bookService.checkAndUpdatePrice(id, price);
            Book book = this.bookService.IsBookExist(id,"MONGO");
            ServerResponse response = new ServerResponse(oldPrice);
            this.loggerHelper.addLogsToUpdateBookPriceEndPoint(id,book.getTitle(),oldPrice,price);
            loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ServerResponse response =new ServerResponse(ex.getMessage());
            loggerHelper.addLogError(ex.getMessage());
            loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        catch (IllegalStateException ex) {
            ServerResponse response =new ServerResponse(ex.getMessage());
            loggerHelper.addLogError(ex.getMessage());
            loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/book", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> updateBookPrice(@RequestParam Integer id)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/book","DELETE");
        try {
            Book book = this.bookService.IsBookExist(id, "MONGO");
            int numberBooks = this.bookService.checkAndDeleteBook(id);
            ServerResponse response = new ServerResponse(numberBooks);
            loggerHelper.addLogsToDeleteBookEndPoint(id,book.getTitle(),numberBooks);
            loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalStateException ex) {
            ServerResponse response = new ServerResponse(ex.getMessage());
            loggerHelper.addLogError(ex.getMessage());
            loggerHelper.addDebugLogForLogRequests();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/logs/level")
    public String getCurrentLevelLogger(@RequestParam(name = "logger-name") String loggerName) {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/logs/level","GET");
        try
        {
            String levelLogger = loggerHelper.getLevelOfLogger(loggerName);
            loggerHelper.addDebugLogForLogRequests();
            return levelLogger;
        }
        catch (Exception ex)
        {
            loggerHelper.addDebugLogForLogRequests();
            return ex.getMessage();
        }
    }
    @PutMapping ("/logs/level")
    public String setCurrentLevelLogger(@RequestParam(name = "logger-name") String loggerName,
                                        @RequestParam(name = "logger-level") String loggerLevel)
    {
        loggerHelper.initStartTimeCurrentReq();
        loggerHelper.addRequest();
        loggerHelper.addInfoLogForLogRequests("/logs/level","PUT");
        try
        {
            String levelLogger = loggerHelper.setLevelOfLogger(loggerName,loggerLevel);
            loggerHelper.addDebugLogForLogRequests();
            return levelLogger;
        }
        catch (Exception ex)
        {
            loggerHelper.addDebugLogForLogRequests();
            return ex.getMessage();
        }
    }

}
