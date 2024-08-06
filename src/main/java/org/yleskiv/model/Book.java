package org.yleskiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Entity
@Table(name = "book", schema = "digitalbookkeeping")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String name;
    @Pattern(regexp = "[A-Z][a-z]+")
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    @Temporal(TemporalType.DATE)
    @Column(name = "year")
    private LocalDate published;
    @Temporal(TemporalType.DATE)
    @Column(name = "taken_date")
    private LocalDate takenDate;
    @ManyToOne
    @JoinColumn(name = "taken_by", referencedColumnName = "id")
    public Person person;
}
