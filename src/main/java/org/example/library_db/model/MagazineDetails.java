package org.example.library_db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "magazine_details")
@Getter
@Setter
@NoArgsConstructor
public class MagazineDetails extends Publication {

    @Column(nullable = false, length = 200)
    private String publisher;

    public MagazineDetails(String title, String publisher) {
        super(title);
        this.publisher = publisher;
    }
}