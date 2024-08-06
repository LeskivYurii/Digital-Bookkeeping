package org.yleskiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yleskiv.model.Book;
import org.yleskiv.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByPersonId(long id);

    Optional<Book> findByName(String name);

    List<Book> findAllByNameContainsIgnoreCase(String name);
}
