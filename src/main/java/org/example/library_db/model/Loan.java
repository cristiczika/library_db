package org.example.library_db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "loan_items", joinColumns = @JoinColumn(name = "loan_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ReadableItem> items = new ArrayList<>();

    public Loan(Member member, LocalDate date) {
        this.member = member;
        this.date = date;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setLoan(this);
    }

    public void addItem(ReadableItem item) {
        items.add(item);
    }
}