package org.yleskiv.model;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private long id;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate published;
    private Long userId;
}
