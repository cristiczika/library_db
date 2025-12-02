package org.example.library_db.configuration;

import org.example.library_db.model.*;
import org.example.library_db.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(LibraryService libraryService, AuthorService authorService, BookDetailsService bookDetailsService, MagazineDetailsService magazineDetailsService, BookAuthorService bookAuthorService, ReadableItemService readableItemService, MemberService memberService, LoanService loanService, ReservationService reservationService) {
        return args -> {
            Library[] libs = {
                    new Library("Librarie Centru", "Strada Napoca 23"),
                    new Library("Librarie Marasti", "Strada Fabricii 55"),
                    new Library("Librarie Hasdeu", "Strada Hasdeu 19"),
                    new Library("Librarie Bucuresti Pipera", "Strada Pipera 15"),
                    new Library("Librarie Timisoara", "Strada Unirii 27"),
                    new Library("Librarie Sibiu", "Strada George Cosbuc 60"),
                    new Library("Librarie Sibiu Centru", "Strada 9 Mai 10"),
                    new Library("Librarie Brasov", "Strada Republicii 5"),
                    new Library("Librarie Constanta", "Strada Mamaia 42"),
                    new Library("Librarie Iasi", "Strada Stefan cel Mare 33")
            };

            for (int i = 0; i < 10; i++)
                libs[i] = libraryService.addLibrary(libs[i]);

            Author[] authors = {
                    new Author(null, "Mihai Eminescu", LocalDate.of(1850, 6, 15), null),
                    new Author(null, "Ion Creangă", LocalDate.of(1837, 12, 12), null),
                    new Author(null, "Liviu Rebreanu", LocalDate.of(1885, 9, 9), null),
                    new Author(null, "Camil Petrescu", LocalDate.of(1894, 7, 4), null),
                    new Author(null, "Jane Austen", LocalDate.of(1775, 12, 16), null),
                    new Author(null, "Leo Tolstoy", LocalDate.of(1828, 9, 9), null),
                    new Author(null, "Chimamanda Ngozi", LocalDate.of(1977, 9, 15), null),
                    new Author(null, "Franz Kafka", LocalDate.of(1883, 7, 3), null),
                    new Author(null, "Margaret Atwood", LocalDate.of(1939, 11, 18), null),
                    new Author(null, "Gabriel García Márquez", LocalDate.of(1927, 3, 6), null)
            };

            for (int i = 0; i < 10; i++)
                authors[i] = authorService.addAuthor(authors[i]);

            BookDetails[] books = {
                    new BookDetails("Poezii"),
                    new BookDetails("Amintiri din Copilarie"),
                    new BookDetails("Harry Potter and the Sorcerer's Stone"),
                    new BookDetails("Ion"),
                    new BookDetails("Patul lui Procust"),
                    new BookDetails("Mandrie si Prejudecata"),
                    new BookDetails("Cadavrul Viu"),
                    new BookDetails("Purple Hibiscus"),
                    new BookDetails("Metamorfoza"),
                    new BookDetails("The Handmaid's Tale")
            };

            for (int i = 0; i < 10; i++)
                books[i] = bookDetailsService.addBookDetails(books[i]);

            MagazineDetails[] mags = {
                    new MagazineDetails("National Geographic", "NatGeo"),
                    new MagazineDetails("Science Weekly", "The Guardian"),
                    new MagazineDetails("Time Magazine", "Time Inc."),
                    new MagazineDetails("Forbes", "Forbes Media"),
                    new MagazineDetails("The Economist", "The Economist Group"),
                    new MagazineDetails("Wired", "Condé Nast"),
                    new MagazineDetails("Vogue", "Condé Nast"),
                    new MagazineDetails("Popular Science", "Bonnier Corporation"),
                    new MagazineDetails("Scientific American", "Springer Nature"),
                    new MagazineDetails("Harvard Business Review", "Harvard Business Publishing")
            };

            for (int i = 0; i < 10; i++)
                mags[i] = magazineDetailsService.addMagazineDetails(mags[i]);

            for (int i = 0; i < 10; i++) {
                bookAuthorService.add(new BookAuthor(null, books[i], authors[i]));
            }

            ReadableItem[] items = {
                    new ReadableItem(books[0], libs[0], "BC101", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(books[1], libs[0], "BC102", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(books[2], libs[0], "BC103", ReadableItemStatus.BORROWED),
                    new ReadableItem(books[3], libs[0], "BC104", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(mags[0], libs[0], "BC105", ReadableItemStatus.RESERVED),
                    new ReadableItem(mags[1], libs[0], "BC106", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(mags[2], libs[0], "BC107", ReadableItemStatus.BORROWED),
                    new ReadableItem(mags[3], libs[0], "BC108", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(books[4], libs[0], "BC109", ReadableItemStatus.AVAILABLE),
                    new ReadableItem(books[5], libs[0], "BC110", ReadableItemStatus.RESERVED)
            };

            for (int i = 0; i < 10; i++)
                items[i] = readableItemService.addReadableItem(items[i]);

            Member[] members = {
                    new Member("Ana Popescu", "ana.popescu@gmail.com", libs[0]),
                    new Member("George Ionescu", "georgei@yahoo.com", libs[0]),
                    new Member("Maria Dobre", "maria_dobre@gmail.com", libs[0]),
                    new Member("Ion Vasilescu", "ion1234@gmail.com", libs[1]),
                    new Member("Elena Marinescu", "elenamarinescu@yahoo.com", libs[1]),
                    new Member("Cristian Radu", "cristianradu2004@gmail.com", libs[2]),
                    new Member("Adriana Stan", "adrianastan@gmail.com", libs[3]),
                    new Member("Mihai Petrescu", "mihaipetrescu09@gmail.com", libs[4]),
                    new Member("Raluca Georgescu", "ralucageorgescu@yahoo.com", libs[4]),
                    new Member("Alina Dumitrescu", "alinadumitrescu@yahoo.com", libs[5])
            };

            for (int i = 0; i < 10; i++)
                members[i] = memberService.addMember(members[i]);

            Loan[] loans = new Loan[10];

            for (int i = 0; i < 10; i++) {

                LocalDate loanDate = LocalDate.of(2025, 10, 20).plusDays(i);

                Loan loan = new Loan(members[i], loanDate);

                loan.addItem(items[i]);
                loan.addItem(items[(i + 1) % 10]);

                loans[i] = loanService.addLoan(loan);
            }

            for (int i = 0; i < 10; i++) {
                LocalDate reservationDate = LocalDate.of(2025, 10, 25).plusDays(i);

                ReservationStatus status =
                        (i % 3 == 0) ? ReservationStatus.ACTIVE :
                                (i % 3 == 1) ? ReservationStatus.COMPLETED :
                                        ReservationStatus.CANCELLED;

                Reservation r = new Reservation(members[i], items[i], reservationDate, status);

                r.setLoan(loans[i]);
                loans[i].addReservation(r);

                reservationService.addReservation(r);
                loanService.addLoan(loans[i]);
            }
        };
    }
}