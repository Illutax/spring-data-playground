package tech.dobler.springdataplayground;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.dobler.springdataplayground.domainvalues.PersonId;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    @Transactional
    void canSave() {
        var id = PersonId.create();
        var p = new Person(id, "Hello", "World");
        repository.save(p);

        var person = repository.findById(id);

        //noinspection AssertBetweenInconvertibleTypes
        assertThat(person).hasValue(p)
                .get()
                .isNotSameAs(p);
    }
}