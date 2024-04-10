package tech.dobler.springdataplayground;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dobler.springdataplayground.domainvalues.PersonId;

@Repository
public interface PersonRepository extends JpaRepository<Person, PersonId> {
}
