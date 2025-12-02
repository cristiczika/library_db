package org.example.library_db.service;

import org.example.library_db.model.Loan;
import org.example.library_db.model.Member;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.repository.LoanRepository;
import org.example.library_db.repository.MemberRepository;
import org.example.library_db.repository.ReadableItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loans;
    private final MemberRepository members;
    private final ReadableItemRepository items;

    public LoanService(LoanRepository loans, MemberRepository members, ReadableItemRepository items) {
        this.loans = loans;
        this.members = members;
        this.items = items;
    }

    public Loan addLoan(Loan loan) {
        return loans.save(loan);
    }

    public void update(Long id, Member member, List<Long> itemIds) {
        Loan loan = loans.findById(id).orElse(null);
        if (loan == null) return;

        loan.setMember(member);
        loan.getItems().clear();

        if (itemIds != null) {
            for (Long itemId : itemIds) {
                ReadableItem item = items.findById(itemId).orElse(null);
                if (item != null) {
                    loan.getItems().add(item);
                }
            }
        }

        loans.save(loan);
    }

    public void removeLoan(Long id) {
        loans.findById(id).ifPresent(loans::delete);
    }

    public Loan getLoanById(Long id) {
        return loans.findById(id).orElse(null);
    }

    public List<Loan> getAllLoans() {
        return loans.findAll();
    }
}