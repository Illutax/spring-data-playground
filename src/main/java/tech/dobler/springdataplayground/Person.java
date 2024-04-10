package tech.dobler.springdataplayground;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;
import tech.dobler.springdataplayground.domainvalues.Address;
import tech.dobler.springdataplayground.domainvalues.PersonId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@ToString

public class Person {
    @Id
    private final PersonId id;
    private final String firstName;
    private final String lastName;
    @ElementCollection
    private List<Address> addressList;

    public Person(PersonId id, String firstName, String lastName, List<Address> addressList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressList = addressList;
    }

    /**
     * @deprecated for Hibernate
     */
    @Deprecated(forRemoval = false)
    protected Person() {
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.addressList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
