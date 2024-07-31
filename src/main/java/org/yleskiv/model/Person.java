package org.yleskiv.model;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private long id;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String firstName;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String middleName;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthDate;

}
