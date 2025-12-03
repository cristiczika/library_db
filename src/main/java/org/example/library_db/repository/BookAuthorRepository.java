package org.example.library_db.repository;

import org.example.library_db.model.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    boolean existsByBookIdAndAuthorId(Long bookId, Long authorId);
    boolean existsByBookIdAndAuthorIdAndIdNot(Long bookId, Long authorId, Long id);
    List<BookAuthor> findByAuthorId(Long authorId);
    List<BookAuthor> findByBookId(Long bookId);
}