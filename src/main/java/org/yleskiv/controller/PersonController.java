package org.yleskiv.controller;

import jakarta.validation.Valid;
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
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookDAO;
import org.yleskiv.repository.PersonDAO;
import org.yleskiv.util.PersonValidator;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final PersonValidator personValidator;

    public PersonController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("persons", personDAO.readAll());
        return "/person/persons-list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable(name = "id") long id, Model model) {
        Person person = personDAO.read(id);
        model.addAttribute("person", person);
        model.addAttribute("books", bookDAO.getUserBooks(id));
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
        personDAO.create(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") long id,  Model model) {
        model.addAttribute("person", personDAO.read(id));
        return "/person/person-edit";
    }

    @PutMapping("/{id}")
    public String edit(@ModelAttribute(name = "person") @Valid Person person, BindingResult bindingResul) {
        personValidator.validate(person, bindingResul);
        if (bindingResul.hasErrors()) {
            return "/person/person-edit";
        }
        personDAO.update(person);
        return "redirect:/persons/" + person.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        personDAO.delete(id);
        return "redirect:/persons";
    }

}
