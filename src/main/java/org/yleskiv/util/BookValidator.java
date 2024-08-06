package org.yleskiv.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.yleskiv.model.Book;
import org.yleskiv.repository.BookRepository;

@Component
public class BookValidator implements Validator {

    private final BookRepository bookRepository;

    public BookValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if(bookRepository.findByName(book.getName()).isPresent()) {
            errors.rejectValue("name", "400", "Book with this name already exists!");
        }
    }
}
