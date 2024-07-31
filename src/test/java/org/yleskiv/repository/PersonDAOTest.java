package org.yleskiv.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Person;
import org.yleskiv.rowmapper.PersonRowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonDAOTest {

    private static final long ID = 1L;

    @Mock
    private Person person;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private PersonDAO personDAO;

    @Test
    public void shouldCreate() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setMiddleName("Dabs");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        when(jdbcTemplate.update("INSERT INTO person(first_name, middle_name, last_name, birthdate) values (?, ?, ?, ?)",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate())).thenReturn(1);

        personDAO.create(person);

        verify(jdbcTemplate).update("INSERT INTO person(first_name, middle_name, last_name, birthdate) values (?, ?, ?, ?)",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate());
    }

    @Test
    public void shouldRead() {
      when(jdbcTemplate.query(eq("SELECT * FROM person WHERE id = ?"), any(PersonRowMapper.class), eq(ID)))
              .thenReturn(List.of(person));

      Person actualResult = personDAO.read(ID);

      assertEquals(actualResult, person);

      verify(jdbcTemplate).query(eq("SELECT * FROM person WHERE id = ?"), any(PersonRowMapper.class), eq(ID));
    }

    @Test
    public void shouldThrowExceptionWhenRead() {
        when(jdbcTemplate.query(eq("SELECT * FROM person WHERE id = ?"), any(PersonRowMapper.class), eq(ID)))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->  personDAO.read(ID));

        verify(jdbcTemplate).query(eq("SELECT * FROM person WHERE id = ?"), any(PersonRowMapper.class), eq(ID));
    }

    @Test
    public void shouldDelete() {
        when(jdbcTemplate.update("DELETE from person WHERE id = ?", ID)).thenReturn(1);

        personDAO.delete(ID);

        verify(jdbcTemplate, Mockito.times(1)).update("DELETE from person WHERE id = ?", ID);
    }

    @Test
    public void shouldGetAll() {
        when(jdbcTemplate.query(eq("SELECT * FROM person"), any(PersonRowMapper.class))).thenReturn(List.of(person));

        assertEquals(List.of(person), personDAO.readAll());

        verify(jdbcTemplate).query(eq("SELECT * FROM person"), any(PersonRowMapper.class));
    }

    @Test
    public void shouldByFullName() {
        String firstName = "John";
        String middleName = "Doe";
        String lastName = "Dabs";

        when(jdbcTemplate.query(eq("SELECT * from person where first_name = ? and middle_name = ? and last_name = ?"),
                any(PersonRowMapper.class), eq(firstName), eq(middleName), eq(lastName))).thenReturn(List.of(person));

        assertEquals(person, personDAO.findByFullName(firstName, middleName, lastName));

        verify(jdbcTemplate).query(eq("SELECT * from person where first_name = ? and middle_name = ? and last_name = ?"),
                any(PersonRowMapper.class), eq(firstName), eq(middleName), eq(lastName));
    }

    @Test
    public void shouldUpdate() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setMiddleName("Dabs");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        when(jdbcTemplate.update("UPDATE person set first_name = ?, middle_name = ?, last_name = ?, birthdate = ? where id = ?",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate(), person.getId())).thenReturn(1);

        personDAO.update(person);

        verify(jdbcTemplate).update("UPDATE person set first_name = ?, middle_name = ?, last_name = ?, birthdate = ? where id = ?",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate(), person.getId());
    }

}
