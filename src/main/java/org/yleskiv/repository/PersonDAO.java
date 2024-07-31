package org.yleskiv.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Person;
import org.yleskiv.rowmapper.PersonRowMapper;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("set schema 'digitalbookkeeping';");
    }

    public void create(Person person) {
        jdbcTemplate.update("INSERT INTO person(first_name, middle_name, last_name, birthdate) values (?, ?, ?, ?)",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate());
    }

    public Person read(long id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new PersonRowMapper(), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No person found with id " + id));
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE from person WHERE id = ?", id);
    }

    public void update(Person person) {
        jdbcTemplate.update("UPDATE person set first_name = ?, middle_name = ?, last_name = ?, birthdate = ? where id = ?",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getBirthDate(), person.getId());
    }

    public List<Person> readAll() {
        return jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper());
    }

    public Person findByFullName(String firstName, String middleName, String lastName) {
        return jdbcTemplate.query("SELECT * from person where first_name = ? and middle_name = ? and last_name = ?"
        , new PersonRowMapper(), firstName, middleName, lastName).stream().findFirst().orElse(null);
    }
}
