package tech.dobler.springdataplayground.usertypes;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.EnhancedUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Abstrakte Oberklasse für UserTypes für Fachwerte, die intern ein einzelnes Value nutzen.<br/>
 * <p>
 * Dieser UserType implementiert den {@link EnhancedUserType}. Das ermöglicht die Verwendung dieses UserTypes
 * für Ids.
 *
 * @param <T> Klasse des Fachwertes, die gemapped werden soll.
 * @param <U> (Java-Standard-) Klasse des internen Values.
 */
abstract class AbstractSimpleValueObjectUserType<T extends Serializable, U> extends AbstractUserType<T>
        implements EnhancedUserType<T> {

    private static final Map<Class<?>, Function<String, ?>> PARSE_FUNCTIONS = new HashMap<>();

    static {
        PARSE_FUNCTIONS.put(Integer.class, Integer::valueOf);
        PARSE_FUNCTIONS.put(Long.class, Long::valueOf);
        PARSE_FUNCTIONS.put(Float.class, Float::valueOf);
        PARSE_FUNCTIONS.put(Double.class, Double::valueOf);
    }

    private final Class<T> valueObjectClass;

    private final Class<U> internalValueClass;

    AbstractSimpleValueObjectUserType(Class<T> valueObjectClass, Class<U> internalValueClass) {
        super(valueObjectClass, internalValueClass);
        Objects.requireNonNull(valueObjectClass, "valueObjectClass");
        Objects.requireNonNull(internalValueClass, "internalValueClass");

        this.valueObjectClass = valueObjectClass;
        this.internalValueClass = convertPrimitiveType(internalValueClass);
        if (!String.class.equals(this.internalValueClass)
                        || PARSE_FUNCTIONS.containsKey(this.internalValueClass)) {
            throw new IllegalArgumentException("internalValueClass can be handled");
        }
    }

    private static Class convertPrimitiveType(Class internalValueClass) {
        if (long.class == internalValueClass) {
            return Long.class;
        }
        if (int.class == internalValueClass) {
            return Integer.class;
        }
        return internalValueClass;
    }

    protected abstract U toDatabaseValue(T valueObject);

    protected abstract T fromDatabaseValue(U databaseValue);

    @Override
    public T nullSafeGet(ResultSet rs, int index, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        U databaseValue = rs.getObject(index, internalValueClass);
        return databaseValue != null ? fromDatabaseValue(databaseValue) : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, T value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value != null) {
            T valueObject = returnedClass().cast(value);
            st.setObject(index, toDatabaseValue(valueObject), getSqlType());
        } else {
            st.setNull(index, getSqlType());
        }
    }

    @Override
    public String toSqlLiteral(T value) {
        return toString(value);
    }

    @Override
    public String toString(T value) throws HibernateException {
        return toDatabaseValue(value).toString();
    }

    @Override
    public T fromStringValue(CharSequence sequence) throws HibernateException {
        String string = sequence.toString();
        if (String.class.equals(internalValueClass)) {
            return fromDatabaseValue((U) string);
        }
        Function<String, ?> fromStringFunction = PARSE_FUNCTIONS.get(internalValueClass);
        if (fromStringFunction != null) {
            return fromDatabaseValue((U) fromStringFunction.apply(string));
        }
        throw new IllegalStateException("Unsupported internal value class " + internalValueClass + " for user type " + getClass().getSimpleName());
    }

    public Class<T> getValueObjectClass() {
        return valueObjectClass;
    }

    public Class<U> getInternalValueClass() {
        return internalValueClass;
    }
}
