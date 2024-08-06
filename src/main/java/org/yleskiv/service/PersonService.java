package org.yleskiv.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yleskiv.exception.EntityNotFoundException;
import org.yleskiv.model.Person;
import org.yleskiv.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Person update(Person person) {
        return personRepository.save(person);
    }

    public void delete(long id) {
        personRepository.deleteById(id);
    }

    public Person findById(long id) {
        return Optional
                .of(id)
                .flatMap(personRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id " + id));
    }

    public List<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }
}
