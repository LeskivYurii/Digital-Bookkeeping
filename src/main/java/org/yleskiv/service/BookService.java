package org.yleskiv.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;

    public BookService(BookRepository bookRepository, PersonService personService) {
        this.bookRepository = bookRepository;
        this.personService = personService;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return Optional
                .of(id)
                .flatMap(bookRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
    }

    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getUserBooks(long id) {
        return bookRepository.findByPersonId(id);
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public void returnBook(long id) {
        Optional
                .of(id)
                .flatMap(bookRepository::findById)
                .ifPresent(book -> {
                    book.setPerson(null);
                    book.setTakenDate(null);});
    }

    @Transactional
    public void takeBook(long userId, long id) {
        Book book = findById(id);
        book.setTakenDate(LocalDate.now());
        Person person = personService.findById(userId);
        book.setPerson(person);
    }

    @Transactional(readOnly = true)
    public List<Book> search(String query) {
        return bookRepository.findAllByNameContainsIgnoreCase(query);
    }
}
