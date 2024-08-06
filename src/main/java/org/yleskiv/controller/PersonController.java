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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yleskiv.model.Person;
import org.yleskiv.service.BookService;
import org.yleskiv.service.PersonService;
import org.yleskiv.util.PersonValidator;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final BookService bookService;
    private final PersonValidator personValidator;

    public PersonController(PersonService personService, BookService bookService, PersonValidator personValidator) {
        this.personService = personService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String getAll(Model model, @PageableDefault Pageable pageable) {
        model.addAttribute("persons", personService.findAll(pageable));
        return "/person/persons-list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable(name = "id") long id, Model model) {
        Person person = personService.findById(id);
        model.addAttribute("person", person);
        model.addAttribute("books", bookService.getUserBooks(id));
        return "/person/person-details";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute Person person) {
        return "/person/person-create";
    }

    @PostMapping
    public String create(@ModelAttribute(name = "person") @Valid Person person, BindingResult bindingResul) {
        personValidator.validate(person, bindingResul);
        if (bindingResul.hasErrors()) {
            return "/person/person-create";
        }
        personService.create(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") long id,  Model model) {
        model.addAttribute("person", personService.findById(id));
        return "/person/person-edit";
    }

    @PutMapping("/{id}")
    public String edit(@ModelAttribute(name = "person") @Valid Person person, BindingResult bindingResul) {
        personValidator.validate(person, bindingResul);
        if (bindingResul.hasErrors()) {
            return "/person/person-edit";
        }
        personService.update(person);
        return "redirect:/persons/" + person.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        personService.delete(id);
        return "redirect:/persons";
    }

}
