package org.yleskiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person", schema = "digitalbookkeeping")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp = "[A-Z][a-z]+")
    @Column(name = "first_name")
    private String firstName;
    @Pattern(regexp = "[A-Z][a-z]+")
    @Column(name = "middle_name")
    private String middleName;
    @Pattern(regexp = "[A-Z][a-z]+")
    @Column(name = "last_name")
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    @Column(name = "birthdate")
    private LocalDate birthDate;
    @OneToMany(mappedBy = "person")
    private List<Book> books = new ArrayList<>();

}
