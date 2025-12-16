package org.example.library_db.repository;

import org.example.library_db.model.BookAuthor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    boolean existsByBookIdAndAuthorId(Long bookId, Long authorId);
    boolean existsByBookIdAndAuthorIdAndIdNot(Long bookId, Long authorId, Long id);
    List<BookAuthor> findByAuthorId(Long authorId);
    List<BookAuthor> findByBookId(Long bookId);
    @Query("""
        select ba from BookAuthor ba
        where lower(ba.author.name) like lower(concat('%', :name, '%'))
    """)
    List<BookAuthor> findByAuthorName(@Param("name") String name, Sort sort);

    @Query("""
        select ba from BookAuthor ba
        where lower(ba.book.title) like lower(concat('%', :title, '%'))
    """)
    List<BookAuthor> findByBookTitle(@Param("title") String title, Sort sort);
}