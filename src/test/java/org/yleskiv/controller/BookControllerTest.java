package org.yleskiv.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.service.BookService;
import org.yleskiv.service.PersonService;
import org.yleskiv.util.BookValidator;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final long ID = 3;
    private static final long USER_ID = 1;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Person person;
    @MockBean
    private Book book;
    @MockBean
    private PersonService personService;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookValidator bookValidator;

    @Test
    public void testGetaAll() throws Exception {
        when(bookService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(book)));

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-list"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", new PageImpl<>(List.of(book))));

        verify(bookService).findAll(any(Pageable.class));
    }

    @Test
    public void shouldGetBookById() throws Exception {
        when(bookService.findById(ID)).thenReturn(book);
        when(personService.findAll(any(Pageable.class))).thenReturn(List.of(person));
        when(book.getPerson()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-details"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", book))
                .andExpect(MockMvcResultMatchers.model().attribute("users", List.of(person)));

        verify(bookService).findById(ID);
        verify(personService).findAll(any(Pageable.class));
        verify(book).getPerson();
    }

    @Test
    public void shouldReturnCreatePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-create"));
    }

    @Test
    public void shouldCreateBook() throws Exception {
        when(bookService.create(any(Book.class))).thenReturn(book);
        doNothing().when(bookValidator).validate(any(Book.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .param("name", "Cinderella")
                        .param("Author", "Dontknow")
                        .param("published", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

        verify(bookService).create(any(Book.class));
        verify(bookValidator).validate(any(Book.class), any(BindingResult.class));
    }
    @Test
    public void shouldThrowValidationErrorCreateBook() throws Exception {
        doNothing().when(bookValidator).validate(any(Book.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .param("name", "Cinderella")
                        .param("Author", "Dontknow")
                        .param("published", "2223-11-12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-create"));

        verify(bookValidator).validate(any(Book.class), any(BindingResult.class));
    }

    @Test
    public void shouldReturnEditPage() throws Exception {
        when(bookService.findById(ID)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + ID + "/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-edit"));

        verify(bookService).findById(ID);
    }

    @Test
    public void shouldUpdateBook() throws Exception {
        when(bookService.update(any(Book.class))).thenReturn(book);
        doNothing().when(bookValidator).validate(any(Book.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + ID)
                        .param("name", "Cinderella")
                        .param("Author", "Dontknow")
                        .param("published", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

        verify(bookService).update(any(Book.class));
        verify(bookValidator).validate(any(Book.class), any(BindingResult.class));
    }

    @Test
    public void shouldThrowValidationErrorWhenUpdateBook() throws Exception {
        doNothing().when(bookValidator).validate(any(Book.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + ID)
                        .param("name", "Cinderella")
                        .param("Author", "Dontknow")
                        .param("published", "2223-11-12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/book/book-edit"));

        verify(bookValidator).validate(any(Book.class), any(BindingResult.class));
    }

    @Test
    public void shouldDelete() throws Exception {
        doNothing().when(bookService).delete(ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + ID))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

        verify(bookService).delete(ID);
    }

    @Test
    public void shouldReturnBook() throws Exception {
        doNothing().when(bookService).returnBook(ID);

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + ID + "/return"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + ID));

        verify(bookService).returnBook(ID);
    }

    @Test
    public void shouldTakeBook() throws Exception {
        doNothing().when(bookService).takeBook(USER_ID, ID);

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + ID + "/take")
                        .param("user_id", String.valueOf(USER_ID)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + ID));

        verify(bookService).takeBook(USER_ID, ID);
    }

}
