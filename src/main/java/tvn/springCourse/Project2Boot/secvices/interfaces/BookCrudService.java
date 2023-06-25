package tvn.springCourse.Project2Boot.secvices.interfaces;

import org.springframework.data.domain.Pageable;
import tvn.springCourse.Project2Boot.exceptions.BookDaoException;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;


import java.util.List;

public interface BookCrudService {
    List<Book> getAll(Pageable pageable);

    List<Book> getAllSortByYear();

    List<Book> getAll();

    List<Book> findByNameStartingWith(String query);

    Book getById(int id) throws BookDaoException;

    void update(Book book);

    void save(Book book);

    void delete(int id);

    Person getBookOwner(int id) throws PersonDaoException, BookDaoException;
}
