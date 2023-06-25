package tvn.springCourse.Project2Boot.secvices.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tvn.springCourse.Project2Boot.enums.DaoErrorCode;
import tvn.springCourse.Project2Boot.exceptions.BookDaoException;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;
import tvn.springCourse.Project2Boot.repositories.BookRepository;
import tvn.springCourse.Project2Boot.repositories.PersonRepository;
import tvn.springCourse.Project2Boot.secvices.interfaces.BookAdministration;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService implements BookAdministration {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }


    @Override
    public List<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> getAllSortByYear() {
        return bookRepository.findAll(Sort.by("publicationYear"));
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByNameStartingWith(String query) {
        return bookRepository.findByNameStartingWith(query);
    }

    public Book getById(int id) throws BookDaoException {
        return bookRepository.findById(id).orElseThrow(() -> new BookDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
    }

    @Transactional
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Person getBookOwner(int id) throws PersonDaoException, BookDaoException {
        Optional<Book> optionalBookFromDb = bookRepository.findById(id);
        if (optionalBookFromDb.isEmpty()) {
            throw new BookDaoException(DaoErrorCode.ENTITY_NOT_FOUND);
        }
        Person bookOwner = optionalBookFromDb.get().getOwner();
        if (Objects.isNull(bookOwner)) {
            throw new PersonDaoException(DaoErrorCode.ENTITY_NOT_FOUND);
        }
        return bookOwner;
    }


    @Transactional
    @Override
    public void releaseBook(int id) throws BookDaoException {
        Book bookFromDb = bookRepository.findById(id).orElseThrow(() -> new BookDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
        bookFromDb.setOwner(null);

    }

    @Transactional
    @Override
    public void lendBook(int book_id, int person_id) throws BookDaoException, PersonDaoException {
        Book bookFromDb = bookRepository.findById(book_id).orElseThrow(() -> new BookDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
        Person personFromDb = personRepository.findById(person_id).orElseThrow(() -> new PersonDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
        bookFromDb.setOwner(personFromDb);
        bookFromDb.setDateOfIssue(new Date());
    }

}
