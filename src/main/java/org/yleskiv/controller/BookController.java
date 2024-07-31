package org.yleskiv.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookDAO;
import org.yleskiv.repository.PersonDAO;
import org.yleskiv.util.BookValidator;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    @GetMapping("/create")
    public String createGet(@ModelAttribute(name = "book") Book book) {
        return "/book/book-create";
    }

    @PostMapping
    public String createPost(@ModelAttribute @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/book/book-create";
        }
        bookDAO.create(book);
        return "redirect:/books";
    }

    @GetMapping
    public String listGet(Model model) {
        model.addAttribute("books", bookDAO.readAll());
        return "/book/book-list";
    }

    @GetMapping("/{id}/edit")
    public String editGet(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("book", bookDAO.read(id));
        return "/book/book-edit";
    }

    @PutMapping("/{id}")
    public String edit(@ModelAttribute @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/book/book-edit";
        }
        bookDAO.update(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String read(@PathVariable(name = "id") long id, Model model) {
        Book book = bookDAO.read(id);
        model.addAttribute("book", book);
        if(book.getUserId() != null && book.getUserId() != 0) {
            model.addAttribute("person", personDAO.read(book.getUserId()));
        } else {
            model.addAttribute("users", personDAO.readAll());
        }
        return "/book/book-details";
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable(name = "id") long id) {
        bookDAO.returnBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/take")
    public String takeBook(@PathVariable(name = "id") long id, @RequestParam(name = "user_id") long userId) {
        bookDAO.takeBook(userId, id);
        return "redirect:/books/" + id;
    }

}
