package org.example.library_db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "magazine_details")
@Getter
@Setter
@NoArgsConstructor
public class MagazineDetails extends Publication {

    @NotBlank(message = "Publisher cannot be empty")
    @Size(min = 3, max = 200)
    @Column(nullable = false, length = 200)
    private String publisher;

    public MagazineDetails(String title, String publisher) {
        super(title);
        this.publisher = publisher;
    }
}