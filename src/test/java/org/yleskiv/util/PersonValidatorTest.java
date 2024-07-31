package org.yleskiv.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;
import org.yleskiv.repository.PersonDAO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonValidatorTest {

    @Mock
    private BindingResult bindingResult;
    @Mock
    private PersonDAO personDAO;
    @Mock
    private Person person;
    @InjectMocks
    private PersonValidator personValidator;

    @Test
    public void shouldSupport() {
        assertTrue(personValidator.supports(Person.class));
    }

    @Test
    public void shouldNotSupport() {
        assertFalse(personValidator.supports(Book.class));
    }

    @Test
    public void shouldValidate() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setMiddleName("Las");

        when(personDAO.findByFullName(person.getFirstName(), person.getMiddleName(), person.getLastName()))
                .thenReturn(null);

        personValidator.validate(person, bindingResult);

        verify(personDAO).findByFullName(person.getFirstName(), person.getMiddleName(), person.getLastName());
        verifyNoInteractions(bindingResult);
    }

    @Test
    public void shouldFailureValidation() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setMiddleName("Las");

        doNothing().when(bindingResult).rejectValue("lastName", "400", "User with that full name already exists!");
        when(personDAO.findByFullName(person.getFirstName(), person.getMiddleName(), person.getLastName()))
                .thenReturn(person);

        personValidator.validate(person, bindingResult);

        verify(personDAO).findByFullName(person.getFirstName(), person.getMiddleName(), person.getLastName());
        verify(bindingResult).rejectValue("lastName", "400", "User with that full name already exists!");
    }

}
