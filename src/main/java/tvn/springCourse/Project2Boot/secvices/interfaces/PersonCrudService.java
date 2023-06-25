package tvn.springCourse.Project2Boot.secvices.interfaces;



import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonCrudService {

    List<Person> getAll();

    Person getById(int id) throws PersonDaoException;

    void update(Person person);

    void save(Person newPerson);

    void delete(int id);

    List<Book> getPersonsBook(int id) throws PersonDaoException;
    Optional<Person> findByFullName(String fullName);

}
