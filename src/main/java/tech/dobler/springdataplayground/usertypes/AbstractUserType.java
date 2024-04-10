package tech.dobler.springdataplayground.usertypes;

import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Types;
import java.util.Objects;

/**
 * Oberklasse für UserTypes, die unveränderliche (immutable) Objekte mappen.<br/>
 *
 * @param <T> Typ, für den der UserType verwendet wird.
 */
abstract class AbstractUserType<T> implements UserType<T> {

    private final Class<T> javaType;

    private final int sqlType;

    /**
     * Dieser Konstruktor ermittelt aus der internalValueClass den passenden
     * SQL-Type
     *
     * @param javaType           Typ, für den der UserType verwendet wird
     * @param internalValueClass Interne repräsentation des Typs
     */
    protected AbstractUserType(Class<T> javaType, Class<?> internalValueClass) {
        Objects.requireNonNull(javaType, "javaType");
        Objects.requireNonNull(internalValueClass, "internalValueClass");
        this.javaType = javaType;
        this.sqlType = sqlType(internalValueClass);
    }

    AbstractUserType(Class<T> javaType, int sqlType) {
        Objects.requireNonNull(javaType, "idClass");

        this.javaType = javaType;
        this.sqlType = sqlType;
    }

    private int sqlType(Class<?> internalValueClass) {
        if (internalValueClass == String.class) {
            return Types.VARCHAR;
        }
        if (internalValueClass == Integer.class || internalValueClass == int.class) {
            return Types.INTEGER;
        }
        if (internalValueClass == Long.class || internalValueClass == long.class) {
            return Types.BIGINT;
        }
        if (internalValueClass == Double.class) {
            return Types.DOUBLE;
        }
        if (internalValueClass == Float.class) {
            return Types.FLOAT;
        }

        throw new AssertionError(
                "SqlType mapping for class " + internalValueClass.getSimpleName() + " not implemented yet");
    }

    @Override
    public T assemble(Serializable cached, Object owner) {
        // simple implementation because value is immutable
        return (T) cached;
    }

    @Override
    public T deepCopy(T value) {
        // simple implementation because value is immutable
        return value;
    }

    @Override
    public Serializable disassemble(T value) {
        return (Serializable) value;
    }

    @Override
    public boolean equals(T a, T b) {
        return Objects.equals(a, b);
    }

    @Override
    public int hashCode(T value) {
        return value.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public T replace(T original, T target, Object owner) {
        return original;
    }

    @Override
    public int getSqlType() {
        return sqlType;
    }

    @Override
    public Class<T> returnedClass() {
        return javaType;
    }
}
