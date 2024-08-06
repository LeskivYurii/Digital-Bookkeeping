package org.yleskiv.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookRepository;

import java.util.Optional;

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
    private BookRepository bookRepository;
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

        when(bookRepository.findByName(book.getName())).thenReturn(Optional.empty());

        bookValidator.validate(book, bindingResult);

        verify(bookRepository).findByName(book.getName());
        verifyNoInteractions(bindingResult);
    }

    @Test
    public void shouldFailureValidation() {
        Book book = new Book();
        book.setName("Test Book");

        doNothing().when(bindingResult).rejectValue("name", "400", "Book with this name already exists!");
        when(bookRepository.findByName(book.getName())).thenReturn(Optional.of(book));

        bookValidator.validate(book, bindingResult);

        verify(bookRepository).findByName(book.getName());
        verify(bindingResult).rejectValue("name", "400", "Book with this name already exists!");
    }
}
