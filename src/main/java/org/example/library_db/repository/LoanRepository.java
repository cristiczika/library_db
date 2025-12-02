package org.example.library_db.repository;

import org.example.library_db.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countTotalItemsByMemberId(Long memberId);
    Loan findTopByMemberIdOrderByDateDesc(Long memberId);
    boolean existsByMemberIdAndItemsId(Long memberId, Long itemId);
}