package org.yleskiv.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.rowmapper.BookRowMapper;
import org.yleskiv.rowmapper.PersonRowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookDAOTest {

    private static final long ID = 1L;
    private static final long USER_ID = 3L;

    @Mock
    private Book book;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private BookDAO bookDAO;

    @Test
    public void shouldCreate() {
        Book book = new Book();
        book.setName("Dane");
        book.setAuthor("Dane Author");
        book.setPublished(LocalDate.of(1990,2,1));

        when(jdbcTemplate.update("INSERT INTO book(name, author, year) values (?, ?, ?)", book.getName(),
                book.getAuthor(), book.getPublished())).thenReturn(1);

        bookDAO.create(book);

        verify(jdbcTemplate).update("INSERT INTO book(name, author, year) values (?, ?, ?)", book.getName(),
                book.getAuthor(), book.getPublished());
    }

    @Test
    public void shouldRead() {
        when(jdbcTemplate.query(eq("SELECT * FROM book WHERE id = ?"), any(BookRowMapper.class), eq(ID)))
                .thenReturn(List.of(book));

        Book actualResult = bookDAO.read(ID);

        assertEquals(actualResult, book);

        verify(jdbcTemplate).query(eq("SELECT * FROM book WHERE id = ?"), any(BookRowMapper.class), eq(ID));
    }

    @Test
    public void shouldThrowExceptionWhenRead() {
        when(jdbcTemplate.query(eq("SELECT * FROM book WHERE id = ?"), any(BookRowMapper.class), eq(ID)))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->  bookDAO.read(ID));

        verify(jdbcTemplate).query(eq("SELECT * FROM book WHERE id = ?"), any(BookRowMapper.class), eq(ID));
    }

    @Test
    public void shouldDelete() {
        when(jdbcTemplate.update("DELETE from book WHERE id = ?", ID)).thenReturn(1);

        bookDAO.delete(ID);

        verify(jdbcTemplate, Mockito.times(1)).update("DELETE from book WHERE id = ?", ID);
    }

    @Test
    public void shouldGetAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM book"), any(BookRowMapper.class))).thenReturn(List.of(book));

        assertEquals(List.of(book), bookDAO.readAll());

        verify(jdbcTemplate).query(eq("SELECT * FROM book"), any(BookRowMapper.class));
    }

    @Test
    public void shouldFindByName() {
        String namne = "John";

        when(jdbcTemplate.query(eq("select * from book where name = ?"), any(BookRowMapper.class),
                eq(namne))).thenReturn(List.of(book));

        assertEquals(book, bookDAO.findByName(namne));

        verify(jdbcTemplate).query(eq("select * from book where name = ?"), any(BookRowMapper.class),
                eq(namne));
    }

    @Test
    public void shouldTakeBook() {
        when(jdbcTemplate.update("UPDATE book SET taken_by = ? WHERE id = ?", USER_ID, ID))
                .thenReturn(1);

        bookDAO.takeBook(USER_ID, ID);

        verify(jdbcTemplate).update("UPDATE book SET taken_by = ? WHERE id = ?", USER_ID, ID);
    }

    @Test
    public void shouldReturnBook() {
        when(jdbcTemplate.update("UPDATE book SET taken_by = null WHERE id = ?", ID))
                .thenReturn(1);

        bookDAO.returnBook(ID);

        verify(jdbcTemplate).update("UPDATE book SET taken_by = null WHERE id = ?", ID);
    }

    @Test
    public void shouldUpdateBook() {
        Book book = new Book();
        book.setName("Dane");
        book.setAuthor("Dane Author");
        book.setPublished(LocalDate.of(1990,2,1));

        when(jdbcTemplate.update("UPDATE book set name = ?, author = ?, year = ?, taken_by = ? where id = ?",
                book.getName(), book.getAuthor(), book.getPublished(), book.getUserId(), book.getId())).thenReturn(1);

        bookDAO.update(book);

        verify(jdbcTemplate).update("UPDATE book set name = ?, author = ?, year = ?, taken_by = ? where id = ?",
                book.getName(), book.getAuthor(), book.getPublished(), book.getUserId(), book.getId());
    }

    @Test
    public void shouldGet() {
        when(jdbcTemplate.query(eq("SELECT * FROM book WHERE taken_by = ?"), any(BookRowMapper.class), eq(USER_ID)))
                .thenReturn(List.of(book));

        assertEquals(List.of(book), bookDAO.getUserBooks(USER_ID));

        verify(jdbcTemplate).query(eq("SELECT * FROM book WHERE taken_by = ?"), any(BookRowMapper.class), eq(USER_ID));
    }
}
