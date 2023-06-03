package dev.rickyshd.sqlwrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SQLConnection {
    /**
     * Prepare a new SQL connection to a database.
     * @param host The remote or local host the database is running on. Use {@code localhost} for local
     *             database applications.
     * @param port The port the database is running on. Default is {@code 3306}.
     * @param username The username to login as in the database.
     * @param password The password corresponding to the given user.
     * @param database The name of the database to connect to.
     * @return A new {@link SQLConnection} instance that is not connected yet.
     */
    static @NotNull SQLConnection to(@NotNull String host, int port, @NotNull String username,
                                     @NotNull String password, @NotNull String database) {
        return new SQLConnectionImpl(host, port, username, password, database);
    }

    /**
     * Connect to the database using the configured settings.
     * @return True on success, false otherwise.
     */
    boolean connect();

    /**
     * Test the connection as done through {@link SQLConnection#connect()} but not in a permanent way.
     * This method is provided just to test the connection to the database. The SQLConnection will remain in
     * an unconnected state.
     * @return True on success, false otherwise.
     */
    boolean testConnect();

    /**
     * Execute a query using the provided arguments.
     * @param query The query to execute on the database attached to this SQLConnection. Use {@code '?'}
     *              for placeholder arguments.
     * @param arguments Replacements for placeholder arguments defined in the {@code query} parameter.
     *                  Order dependant.
     * @deprecated 1.2.0
     * @return An {@link Optional} object that contains a {@link Map} of {@link String} labels in
     * {@link List<Object>} values. Each label represents a column name in the result table; the list
     * attached to it contains all the value present in the column. The {@link Optional} may be empty
     * on empty result tables.
     */
    @Deprecated
    Optional<Map<String, List<Object>>> executeQueryByColumn(@NotNull String query, Object... arguments);

    /**
     * Execute a query using the provided arguments.
     * @param query The query to execute on the database attached to this SQLConnection. Use {@code '?'}
     *              for placeholder arguments.
     * @param arguments Replacements for placeholder arguments defined in the {@code query} parameter.
     *                  Order dependant.
     * @since 1.2.0
     * @return A {@link List} of {@link SQLRow} objects. Each {@link SQLRow} object represents a single
     * row in the result table. The list is empty on empty result tables.
     */
    List<SQLRow> executeQuery(@NotNull String query, Object... arguments);

    /**
     * Execute a statement using the provided arguments. A statement is a query that does not
     * generate any result table. Useful for {@code 'INSERT'} or {@code 'ALTER'} queries.
     * @param statement The statement to execute on the database attached to this SQLConnection.
     *                  Use {@code '?'} for placeholder arguments.
     * @param arguments Replacements for placeholder arguments defined in the query parameter.
     *                  Order dependant.
     * @return True on successful statement, false otherwise.
     */
    boolean executeStatement(@NotNull String statement, Object... arguments);

    /**
     * Execute a pre-made SQL script.
     * @param path The path to the SQL script.
     * @return True on success, false otherwise.
     */
    boolean executeFromFile(@NotNull String path);
}
