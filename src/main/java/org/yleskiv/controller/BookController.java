package org.yleskiv.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.yleskiv.service.BookService;
import org.yleskiv.service.PersonService;
import org.yleskiv.util.BookValidator;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;
    private final BookValidator bookValidator;

    public BookController(BookService bookService, PersonService personService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.personService = personService;
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
        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping
    public String listGet(Model model, @PageableDefault Pageable pageable) {
        model.addAttribute("books", bookService.findAll(pageable));
        return "/book/book-list";
    }

    @GetMapping("/{id}/edit")
    public String editGet(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "/book/book-edit";
    }

    @PutMapping("/{id}")
    public String edit(@ModelAttribute @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/book/book-edit";
        }
        bookService.update(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String read(@PathVariable(name = "id") long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        if(book.getPerson() != null) {
            model.addAttribute("person", personService.findById(book.getPerson().getId()));
        } else {
            model.addAttribute("users", personService.findAll(Pageable.unpaged()));
        }
        return "/book/book-details";
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable(name = "id") long id) {
        bookService.returnBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/take")
    public String takeBook(@PathVariable(name = "id") long id, @RequestParam(name = "user_id") long userId) {
        bookService.takeBook(userId, id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchGet(Model model, @RequestParam(required = false, name = "query") String query) {
        model.addAttribute("books", bookService.search(query));
        return "/book/book-search";
    }

}
