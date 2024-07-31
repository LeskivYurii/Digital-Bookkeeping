package org.yleskiv.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.yleskiv.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setName(rs.getString("name"));
        book.setAuthor(rs.getString("author"));
        book.setPublished(rs.getDate("year").toLocalDate());
        book.setUserId(rs.getLong("taken_by"));
        return book;
    }
}
