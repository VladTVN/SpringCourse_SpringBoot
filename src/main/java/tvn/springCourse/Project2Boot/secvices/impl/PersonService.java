package tvn.springCourse.Project2Boot.secvices.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tvn.springCourse.Project2Boot.enums.DaoErrorCode;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;
import tvn.springCourse.Project2Boot.repositories.PersonRepository;
import tvn.springCourse.Project2Boot.secvices.interfaces.PersonCrudService;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService implements PersonCrudService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getById(int id) throws PersonDaoException {
        return personRepository.findById(id).orElseThrow(() -> new PersonDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
    }

    @Transactional
    public void update(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void save(Person newPerson) {
        personRepository.save(newPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }


    @Override
    public List<Book> getPersonsBook(int id) throws PersonDaoException {
        Person person = personRepository.findById(id).orElseThrow(() -> new PersonDaoException(DaoErrorCode.ENTITY_NOT_FOUND));
        List<Book> personsBookList = person.getBookList();
        for (Book book : personsBookList) {
            Date dateOfIssue = book.getDateOfIssue();
            if (Objects.isNull(dateOfIssue)){
                continue;
            }
            Date currentDate = new Date();
            long dateDifference = currentDate.getTime() - dateOfIssue.getTime();
            long differencesInDays = Math.abs(dateDifference / (24 * 60 * 60 * 1000));
            if (differencesInDays > 10) {
                book.setReturnPeriodExpired(true);
            }

        }
        return personsBookList;
    }


    @Override
    public Optional<Person> findByFullName(String fullName) {
        return personRepository.findByFullName(fullName);
    }

}
