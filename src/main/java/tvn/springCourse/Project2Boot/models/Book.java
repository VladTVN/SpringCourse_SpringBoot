package tvn.springCourse.Project2Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    @NotEmpty(message = "Name cant ba empty")
    @Size(min = 3, max = 30, message = "Name mast be between 3 and 30 symbols")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "author cant ba empty")
    @Size(min = 3, max = 30, message = "author mast be between 3 and 30 symbols")
    @Column(name = "author")
    private String author;
    @Min(value = 0, message = "Age cant be less then 0")
    @Column(name = "publication_date")
    private int publicationYear;

    @Column(name = "date_of_issue")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateOfIssue;

    @Transient
    private boolean returnPeriodExpired = false;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;


    public Book(int bookId, String name, String author, int publicationYear) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public Book() {
    }

    public Book(String name, String author, int publicationYear, Date dateOfIssue, boolean returnPeriodExpired, Person owner) {
        this.name = name;
        this.author = author;
        this.publicationYear = publicationYear;
        this.dateOfIssue = dateOfIssue;
        this.returnPeriodExpired = returnPeriodExpired;
        this.owner = owner;
    }

    public boolean isReturnPeriodExpired() {
        return returnPeriodExpired;
    }

    public void setReturnPeriodExpired(boolean returnPeriodExpired) {
        this.returnPeriodExpired = returnPeriodExpired;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationDate) {
        this.publicationYear = publicationDate;
    }
}
