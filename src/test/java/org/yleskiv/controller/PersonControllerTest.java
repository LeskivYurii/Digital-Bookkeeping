package org.yleskiv.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.service.BookService;
import org.yleskiv.service.PersonService;
import org.yleskiv.util.PersonValidator;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {PersonController.class})
public class PersonControllerTest {

    private static final long ID = 3;

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
    private PersonValidator personValidator;

    @Test
    public void testGetaAll() throws Exception {
        when(personService.findAll(any(Pageable.class))).thenReturn(List.of(person));

        mockMvc.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/persons-list"))
                .andExpect(MockMvcResultMatchers.model().attribute("persons", List.of(person)));

        verify(personService).findAll(any(Pageable.class));
    }

    @Test
    public void shouldGetPersonById() throws Exception {
        when(personService.findById(ID)).thenReturn(person);
        when(bookService.getUserBooks(ID)).thenReturn(List.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-details"))
                .andExpect(MockMvcResultMatchers.model().attribute("person", person))
                .andExpect(MockMvcResultMatchers.model().attribute("books", List.of(book)));

        verify(personService).findById(ID);
        verify(bookService).getUserBooks(ID);
    }

    @Test
    public void shouldReturnCreatePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/persons/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-create"));
    }

    @Test
    public void shouldCreatePerson() throws Exception {
        when(personService.create(any(Person.class))).thenReturn(person);
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .param("firstName", "Don")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));

        verify(personService).create(any(Person.class));
        verify(personValidator).validate(any(Person.class), any(BindingResult.class));
    }
    @Test
    public void shouldThrowValidationErrorCreatePerson() throws Exception {
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .param("firstName", "son")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-create"));

        verify(personValidator).validate(any(Person.class), any(BindingResult.class));
    }

    @Test
    public void shouldReturnEditPage() throws Exception {
        when(personService.findById(ID)).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + ID + "/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-edit"));

        verify(personService).findById(ID);
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        when(personService.update(any(Person.class))).thenReturn(person);
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/" + ID)
                        .param("firstName", "Don")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons/" + ID));

        verify(personService).update(any(Person.class));
        verify(personValidator).validate(any(Person.class), any(BindingResult.class));
    }

    @Test
    public void shouldThrowValidationErrorWhenUpdatePerson() throws Exception {
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/" + ID)
                        .param("firstName", "son")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-edit"));

        verify(personValidator).validate(any(Person.class), any(BindingResult.class));
    }

    @Test
    public void shouldDelete() throws Exception {
       doNothing().when(personService).delete(ID);

       mockMvc.perform(MockMvcRequestBuilders.delete("/persons/" + ID))
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));

       verify(personService).delete(ID);
    }

}
