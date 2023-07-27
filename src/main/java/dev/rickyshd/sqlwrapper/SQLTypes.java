package dev.rickyshd.sqlwrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SQLTypes {
    public static final Class<?> TINYINT = Byte.class;
    public static final Class<?> SMALLINT = Short.class;
    public static final Class<?> YEAR = Short.class;
    public static final Class<?> INT = Integer.class;
    public static final Class<?> BIGINT = Long.class;
    public static final Class<?> BIGINT_UNSIGNED = Long.class;
    public static final Class<?> FLOAT = Float.class;
    public static final Class<?> DOUBLE = Double.class;
    public static final Class<?> DATETIME = Date.class;
    public static final Class<?> TIMESTAMP = Date.class;
    public static final Class<?> TIME = Date.class;
    public static final Class<?> DATE = Date.class;
    public static final Class<?> CHAR = String.class;
    public static final Class<?> VARCHAR = String.class;
    public static final Class<?> TEXT = String.class;
    public static final Class<?> BINARY = byte[].class;
    public static final Class<?> VARBINARY = byte[].class;
    public static final Class<?> BLOB = byte[].class;

    public static Class<?> NUMERIC(int precision) {
        return (precision == 0) ? BigInteger.class : BigDecimal.class;
    }

    public static Class<?> DECIMAL(int precision) {
        return (precision == 0) ? BigInteger.class : BigDecimal.class;
    }

    public static Class<?> BIT(int n) {
        if (n > 32) return Long.class;
        if (n > 16) return Integer.class;
        if (n > 8) return Short.class;
        if (n > 1) return Byte.class;
        else return Boolean.class;
    }
}
