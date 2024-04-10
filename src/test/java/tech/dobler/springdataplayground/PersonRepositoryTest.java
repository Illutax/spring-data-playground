package tech.dobler.springdataplayground;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.dobler.springdataplayground.domainvalues.Address;
import tech.dobler.springdataplayground.domainvalues.PersonId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PersonRepositoryTest {

    private final PersonRepository repository;
    private final EntityManager entityManager;

    @Test
    @Transactional
    void canSave() {
        var id = PersonId.create();
        var p = new Person(id, "Hello", "World", List.of(
                new Address("Hamburger Straße", "123"),
                new Address("Hamburger Straße", "123")
        ));
        repository.save(p);

        var person = repository.findById(id);
        entityManager.flush();
        entityManager.clear();

        //noinspection AssertBetweenInconvertibleTypes
        assertThat(person).hasValue(p)
                .get()
                .isNotSameAs(p);
    }
}