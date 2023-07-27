package dev.rickyshd.sqlwrapper;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SQLRow {

    /**
     * Create a new {@link SQLRow} from a {@link Map} of {@link String} in {@link Object}.
     * @param content The Map containing labels and values.
     * @since 1.2.0
     * @return A new {@link SQLRow} instance.
     */
    static @NotNull SQLRow of(Map<String, Object> content) {
        return new SQLRowImpl(content);
    }

    /**
     * Create an empty {@link SQLRow}.
     * @return A new empty {@link SQLRow}.
     * @since 1.2.0
     */
    static @NotNull SQLRow empty() {
        return new SQLRowImpl(new HashMap<>());
    }

    /**
     * @return True if not empty, false otherise.
     * @since 1.2.0
     */
    boolean isNotEmpty();

    /**
     * Get a set of all the labels stored in this {@link SQLRow} instance. From
     * a Map perspective it's identical to {@link Map#keySet()}
     * @return A {@link Set} of Strings containing all the labels.
     * @since 1.2.0
     */
    Set<String> getLabels();

    /**
     * Get a value from the row using a column label.
     * @param columnLabel The label of the column to get the value from.
     * @return {@code null} if the columnLabel is not present, the Object corresponding
     * to the label otherwise.
     * @since 1.2.0
     */
    Object getValue(String columnLabel);

    <T> T getValue(String columnLabel, Class<T> type);


}

