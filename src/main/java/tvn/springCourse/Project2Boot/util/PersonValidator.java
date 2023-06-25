package tvn.springCourse.Project2Boot.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tvn.springCourse.Project2Boot.models.Person;
import tvn.springCourse.Project2Boot.secvices.interfaces.PersonCrudService;


import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonCrudService personCrudService;

    public PersonValidator(PersonCrudService personCrudService) {
        this.personCrudService = personCrudService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personOptional = personCrudService.findByFullName(person.getFullName());
        if (personOptional.isPresent() && personOptional.get().getPersonId() != person.getPersonId()){
            errors.rejectValue("fullName","", "Человек с таким ФИО уже существует" );
        }
    }
}
