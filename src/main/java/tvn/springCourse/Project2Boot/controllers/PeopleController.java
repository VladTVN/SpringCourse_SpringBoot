package tvn.springCourse.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tvn.springCourse.Project2Boot.enums.DaoErrorCode;
import tvn.springCourse.Project2Boot.exceptions.PersonDaoException;
import tvn.springCourse.Project2Boot.models.Book;
import tvn.springCourse.Project2Boot.models.Person;
import tvn.springCourse.Project2Boot.secvices.interfaces.BookAdministration;
import tvn.springCourse.Project2Boot.secvices.interfaces.PersonCrudService;
import tvn.springCourse.Project2Boot.util.PersonValidator;


import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonCrudService personService;
    private final PersonValidator personValidator;


    public PeopleController(PersonCrudService personService, PersonValidator personValidator, BookAdministration bookAdministration) {
        this.personService = personService;
        this.personValidator = personValidator;
    }


    @GetMapping
    public String getPeopleList(Model model) {
        model.addAttribute("people", personService.getAll());
        return "people/peopleList";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model,
                          @ModelAttribute("person") Person person, BindingResult bindingResult) {

        try {
            person = personService.getById(id);
            model.addAttribute("person", person);
        } catch (PersonDaoException personDAOException) {
            if (personDAOException.getErrorCode().equals(DaoErrorCode.ENTITY_NOT_FOUND)) {
                bindingResult.rejectValue("id", "", personDAOException.errorMessage());
            }
        }

        List<Book> personsBooks;
        try {
            personsBooks = personService.getPersonsBook(id);
        } catch (PersonDaoException e) {
            //TODO Сделать обработку ошибки. Возможно, сделать страницу ошибки
            throw new RuntimeException(e);
        }
        model.addAttribute("books", personsBooks);

        return "people/person";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person, BindingResult bindingResult) {


        try {
            person = personService.getById(id);
            model.addAttribute("person", person);
        } catch (PersonDaoException personDAOException) {
            bindingResult.rejectValue("id", "", personDAOException.errorMessage());
        }

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personService.update(person);
        return "redirect:/people";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String newPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }

}
