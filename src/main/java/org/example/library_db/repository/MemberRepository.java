package org.example.library_db.repository;

import org.example.library_db.model.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    List<Member> findByLibraryId(Long libraryId);

    List<Member> findByNameContainingIgnoreCase(String name, Sort sort);

    List<Member> findByEmailContainingIgnoreCase(String email, Sort sort);

    @Query("""
        SELECT m
        FROM Member m
        JOIN m.library l
        WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :library, '%'))
    """)
    List<Member> findByLibraryName(@Param("library") String library, Sort sort);

    @Query("""
        SELECT m
        FROM Member m
        JOIN m.library l
        WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))
          AND LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))
          AND LOWER(l.name) LIKE LOWER(CONCAT('%', :library, '%'))
    """)
    List<Member> filterAll(
            @Param("name") String name,
            @Param("email") String email,
            @Param("library") String library,
            Sort sort
    );
}