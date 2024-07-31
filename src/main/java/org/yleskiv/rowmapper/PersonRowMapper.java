package org.yleskiv.rowmapper;


import org.springframework.jdbc.core.RowMapper;
import org.yleskiv.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setMiddleName(rs.getString("middle_name"));
        person.setLastName(rs.getString("last_name"));
        person.setBirthDate(rs.getDate("birthdate").toLocalDate());

        return person;
    }
}
