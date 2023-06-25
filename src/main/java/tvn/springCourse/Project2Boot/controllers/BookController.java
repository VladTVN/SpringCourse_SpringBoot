package tvn.springCourse.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tvn.springCourse.Project2Boot.enums.DaoErrorCode;
import tvn.springCourse.Project2Boot.exceptions.BookDaoException;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;
import tvn.springCourse.Project2Boot.secvices.interfaces.BookAdministration;
import tvn.springCourse.Project2Boot.secvices.interfaces.PersonCrudService;


import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookAdministration bookAdministration;
    private final PersonCrudService personCrudService;

    @Autowired
    public BookController(BookAdministration bookAdministration, PersonCrudService personCrudService) {
        this.bookAdministration = bookAdministration;
        this.personCrudService = personCrudService;
    }

    @GetMapping
    public String getBookList(Model model,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "books_per_page",required = false) Integer bookPerPage,
                              @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){

        List<Book> bookList;
        if (Objects.nonNull(page) && Objects.nonNull(bookPerPage)){
            Pageable pageRequest;
            if (sortByYear){
                pageRequest = PageRequest.of(page, bookPerPage, Sort.by("publicationYear"));
            }else {
                pageRequest = PageRequest.of(page, bookPerPage);
            }
            bookList = bookAdministration.getAll(pageRequest);
        }else {
            if(sortByYear){
                bookList = bookAdministration.getAllSortByYear();
            }else{
                bookList = bookAdministration.getAll();
            }
        }

        model.addAttribute("books", bookList);
        return "book/books";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model,
                          @ModelAttribute("book") Book book, BindingResult bindingResult, @ModelAttribute("person") Person person){


        try {
            book = bookAdministration.getById(id);
            model.addAttribute("book", book);
            Person owner = bookAdministration.getBookOwner(id);
            model.addAttribute("owner", owner);
        } catch (PersonDaoException personDaoException) {
            if (personDaoException.getErrorCode().equals(DaoErrorCode.ENTITY_NOT_FOUND)) {
                List<Person> people = personCrudService.getAll();
                model.addAttribute("people", people);
            }
        } catch (BookDaoException bookDAOException) {
            if (bookDAOException.getErrorCode().equals(DaoErrorCode.ENTITY_NOT_FOUND)) {
                bindingResult.rejectValue("id", "", bookDAOException.errorMessage());
            }
        }


        return "book/book";
    }

    @PatchMapping("/{id}/lend")
    public String lendBook(@PathVariable("id") int id, @ModelAttribute("person") Person person,
                             @ModelAttribute("book") Book book){

        try {
            bookAdministration.lendBook(id, person.getPersonId());
        } catch (BookDaoException | PersonDaoException e) {
            //TODO Сделать обработку ошибки. Возможно, сделать страницу ошибки
            throw new RuntimeException(e);
        }
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id, @ModelAttribute("book") Book book){

        try {
            bookAdministration.releaseBook(id);
        } catch (BookDaoException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model,
                       @ModelAttribute("book") Book book, BindingResult bindingResult){

        try {
            book = bookAdministration.getById(id);
            model.addAttribute("book",book);
        } catch (BookDaoException bookDAOException) {
            bindingResult.rejectValue("id","",  bookDAOException.errorMessage());
        }

        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "book/edit";
        }
        bookAdministration.update(book);
        return "redirect:/books";
    }

    @GetMapping("new")
    public String newBook(@ModelAttribute("book") Book book){
        return "book/new";
    }

    @PostMapping
    public String newPerson(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "book/new";
        }
        bookAdministration.save(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookAdministration.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String bookSearch(){
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearch(@RequestParam("query") String query, Model model){
        List<Book> bookList = bookAdministration.findByNameStartingWith(query);

        model.addAttribute("books",bookList);
        return "book/search";
    }

}
