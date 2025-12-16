package org.example.library_db.service;

import org.example.library_db.model.Loan;
import org.example.library_db.model.Member;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.model.ReadableItemStatus;
import org.example.library_db.repository.LoanRepository;
import org.example.library_db.repository.MemberRepository;
import org.example.library_db.repository.ReadableItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        validateLoanCreation(loan);
        loan.getItems().forEach(it -> it.setStatus(ReadableItemStatus.BORROWED));
        return loans.save(loan);
    }

    public void update(Long id, Member member, List<Long> itemIds) {
        Loan loan = loans.findById(id).orElse(null);
        if (loan == null) return;

        loan.setMember(member);

        Set<ReadableItem> newItems = new HashSet<>();
        if (itemIds != null) {
            for (Long itemId : itemIds) {
                ReadableItem item = items.findById(itemId).orElse(null);
                if (item != null) {
                    newItems.add(item);
                }
            }
        }

        validateLoanUpdate(loan, newItems);

        for (ReadableItem old : loan.getItems()) {
            if (!newItems.contains(old)) {
                old.setStatus(ReadableItemStatus.AVAILABLE);
            }
        }

        newItems.forEach(i -> i.setStatus(ReadableItemStatus.BORROWED));

        loan.getItems().clear();
        loan.getItems().addAll(newItems);

        loans.save(loan);
    }

    public void removeLoan(Long id) {
        loans.findById(id).ifPresent(loan -> {
            loan.getItems().forEach(i -> i.setStatus(ReadableItemStatus.AVAILABLE));
            loans.delete(loan);
        });
    }

    public Loan getLoanById(Long id) {
        return loans.findById(id).orElse(null);
    }

    public List<Loan> getAllLoans() {
        return loans.findAll();
    }

    public List<Loan> filter(
            String member,
            String barcode,
            LocalDate date,
            String sort,
            String dir
    ) {
        Sort s = dir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        if (member != null && !member.isBlank()) {
            return loans.findByMemberName(member, s);
        }

        if (barcode != null && !barcode.isBlank()) {
            return loans.findByItemBarcode(barcode, s);
        }

        if (date != null) {
            return loans.findByDate(date, s);
        }

        return loans.findAll(s);
    }

    private void validateLoanCreation(Loan loan) {
        if (loan.getMember() == null || loan.getMember().getId() == null) {
            throw new IllegalArgumentException("Loan must have a member.");
        }

        if (loan.getItems() == null || loan.getItems().isEmpty()) {
            throw new IllegalArgumentException("Loan must contain at least one item.");
        }

        if (loan.getDate() == null) {
            loan.setDate(LocalDate.now());
        }

        if (loan.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Loan date cannot be in the future.");
        }

        long alreadyBorrowed = loans.countTotalItemsByMemberId(loan.getMember().getId());
        long newCount = loan.getItems().size();
        if (alreadyBorrowed + newCount > 5) {
            throw new IllegalArgumentException("A member cannot borrow more than 5 items in total.");
        }
    }

    private void validateLoanUpdate(Loan existingLoan, Set<ReadableItem> newItems) {
        if (existingLoan.getMember() == null || existingLoan.getMember().getId() == null) {
            throw new IllegalArgumentException("Loan must have a member.");
        }

        if (newItems.isEmpty()) {
            throw new IllegalArgumentException("Loan must contain at least one item.");
        }

        long alreadyBorrowed = loans.countTotalItemsByMemberId(existingLoan.getMember().getId());
        long oldLoanSize = existingLoan.getItems().size();
        long delta = newItems.size() - oldLoanSize;
        if (alreadyBorrowed + delta > 5) {
            throw new IllegalArgumentException("A member cannot borrow more than 5 items in total.");
        }
    }
}