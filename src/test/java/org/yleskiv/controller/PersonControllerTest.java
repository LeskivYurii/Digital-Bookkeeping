package org.yleskiv.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.BookDAO;
import org.yleskiv.repository.PersonDAO;
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
    private PersonDAO personDAO;
    @MockBean
    private BookDAO bookDAO;
    @MockBean
    private PersonValidator personValidator;

    @Test
    public void testGetaAll() throws Exception {
        when(personDAO.readAll()).thenReturn(List.of(person));

        mockMvc.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/persons-list"))
                .andExpect(MockMvcResultMatchers.model().attribute("persons", List.of(person)));

        verify(personDAO).readAll();
    }

    @Test
    public void shouldGetPersonById() throws Exception {
        when(personDAO.read(ID)).thenReturn(person);
        when(bookDAO.getUserBooks(ID)).thenReturn(List.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-details"))
                .andExpect(MockMvcResultMatchers.model().attribute("person", person))
                .andExpect(MockMvcResultMatchers.model().attribute("books", List.of(book)));

        verify(personDAO).read(ID);
        verify(bookDAO).getUserBooks(ID);
    }

    @Test
    public void shouldReturnCreatePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/persons/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-create"));
    }

    @Test
    public void shouldCreatePerson() throws Exception {
        doNothing().when(personDAO).create(any(Person.class));
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .param("firstName", "Don")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));

        verify(personDAO).create(any(Person.class));
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
        when(personDAO.read(ID)).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/" + ID + "/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/person/person-edit"));

        verify(personDAO).read(ID);
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        doNothing().when(personDAO).update(any(Person.class));
        doNothing().when(personValidator).validate(any(Person.class), any(BindingResult.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/" + ID)
                        .param("firstName", "Don")
                        .param("middleName", "Resan")
                        .param("lastName", "Lenard")
                        .param("birthDay", "1990-11-12"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons/" + ID));

        verify(personDAO).update(any(Person.class));
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
       doNothing().when(personDAO).delete(ID);

       mockMvc.perform(MockMvcRequestBuilders.delete("/persons/" + ID))
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));

       verify(personDAO).delete(ID);
    }

}
