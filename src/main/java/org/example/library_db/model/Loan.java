package org.example.library_db.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

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

    @NotNull(message = "Member is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Valid
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @Valid
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