package tech.dobler.springdataplayground.domainvalues;

import java.io.Serializable;
import java.util.UUID;

public record PersonId(String value) implements Serializable {
    public static PersonId of(String value) {
        return new PersonId(value);
    }
    public static PersonId create() {
        return new PersonId(UUID.randomUUID().toString());
    }
}
