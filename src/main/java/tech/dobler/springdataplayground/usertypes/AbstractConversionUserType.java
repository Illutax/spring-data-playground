package tech.dobler.springdataplayground.usertypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Oberklasse für UserTypes für Fachwerte, die mit passender Factory-Methode und to-Value-Methode konfiguriert wird.
 *
 * @param <T> Klasse des Fachwertes, die gemapped werden soll.
 * @param <U> (Java-Standard-) Klasse des internen Values.
 */
public abstract class AbstractConversionUserType<T extends Serializable, U> extends AbstractSimpleValueObjectUserType<T, U> {
    private final Function<T, U> toDatabaseFunction;
    private final Function<U, T> fromDatabaseFunction;

    public AbstractConversionUserType(
            Class<T> valueObjectClass,
            Class<U> internalValueClass,
            Function<T, U> toDatabaseFunction,
            Function<U, T> fromDatabaseFunction) {
        super(valueObjectClass, internalValueClass);
        Objects.requireNonNull(toDatabaseFunction, "toDatabaseFunction");
        Objects.requireNonNull(fromDatabaseFunction, "fromDatabaseFunction");
        this.toDatabaseFunction = toDatabaseFunction;
        this.fromDatabaseFunction = fromDatabaseFunction;
    }

    @Override
    protected final U toDatabaseValue(T valueObject) {
        return toDatabaseFunction.apply(valueObject);
    }

    @Override
    protected final T fromDatabaseValue(U databaseValue) {
        return fromDatabaseFunction.apply(databaseValue);
    }
}
