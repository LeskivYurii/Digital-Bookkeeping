package org.yleskiv.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.rowmapper.BookRowMapper;
import org.yleskiv.rowmapper.PersonRowMapper;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("set schema 'digitalbookkeeping';");
    }

    public void create(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) values (?, ?, ?)",
                book.getName(), book.getAuthor(), book.getPublished());
    }

    public Book read(long id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?",new BookRowMapper() {}, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No person found with id " + id));
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE from book WHERE id = ?", id);
    }

    public void update(Book book) {
        jdbcTemplate.update("UPDATE book set name = ?, author = ?, year = ?, taken_by = ? where id = ?",
                book.getName(), book.getAuthor(), book.getPublished(), book.getUserId(), book.getId());
    }

    public List<Book> readAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BookRowMapper());
    }

    public List<Book> getUserBooks(long userId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE taken_by = ?", new BookRowMapper(), userId);
    }

    public void returnBook(long id) {
        jdbcTemplate.update("UPDATE book SET taken_by = null WHERE id = ?", id);
    }

    public void takeBook(long userId, long id) {
        jdbcTemplate.update("UPDATE book SET taken_by = ? WHERE id = ?", userId, id);
    }

    public Book findByName(String name) {
        return jdbcTemplate.query("select * from book where name = ?", new BookRowMapper(), name).stream().findFirst().orElse(null);
    }
}
