package tvn.springCourse.Project2Boot.secvices.interfaces;


import tvn.springCourse.Project2Boot.exceptions.BookDaoException;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;

public interface BookAdministration extends BookCrudService{
    void releaseBook(int id) throws BookDaoException;
    void lendBook(int book_id, int person_id) throws BookDaoException, PersonDaoException;


}
