package org.yleskiv.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookDAO;
import org.yleskiv.repository.PersonDAO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookValidatorTest {


    @Mock
    private BindingResult bindingResult;
    @Mock
    private BookDAO bookDAO;
    @Mock
    private Person person;
    @InjectMocks
    private BookValidator bookValidator;

    @Test
    public void shouldSupport() {
        assertTrue(bookValidator.supports(Book.class));
    }

    @Test
    public void shouldNotSupport() {
        assertFalse(bookValidator.supports(Person.class));
    }

    @Test
    public void shouldValidate() {
        Book book = new Book();
        book.setName("Test Book");

        when(bookDAO.findByName(book.getName())).thenReturn(null);

        bookValidator.validate(book, bindingResult);

        verify(bookDAO).findByName(book.getName());
        verifyNoInteractions(bindingResult);
    }

    @Test
    public void shouldFailureValidation() {
        Book book = new Book();
        book.setName("Test Book");

        doNothing().when(bindingResult).rejectValue("name", "400", "Book with this name already exists!");
        when(bookDAO.findByName(book.getName())).thenReturn(book);

        bookValidator.validate(book, bindingResult);

        verify(bookDAO).findByName(book.getName());
        verify(bindingResult).rejectValue("name", "400", "Book with this name already exists!");
    }
}
