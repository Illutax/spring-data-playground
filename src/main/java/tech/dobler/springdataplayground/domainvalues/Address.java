package tech.dobler.springdataplayground.domainvalues;

import java.io.Serializable;

public record Address(String street, String number) implements Serializable {
    private static final String SEPARATOR = "_";

    public String toDb() {
        return street + SEPARATOR + number;
    }
    public static Address fromDb(String dbString) {
        String[] parts = dbString.split(SEPARATOR);
        return new Address(parts[0], parts[1]);
    }
}
