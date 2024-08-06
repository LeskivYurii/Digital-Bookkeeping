package org.yleskiv.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.yleskiv.model.Person;
import org.yleskiv.repository.PersonRepository;

@Component
public class PersonValidator implements Validator {

    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(personRepository.findByFirstNameAndMiddleNameAndLastName(person.getFirstName(), person.getMiddleName(), person.getLastName()).isPresent()) {
            errors.rejectValue("lastName", "400", "User with that full name already exists!");
        }
    }
}
