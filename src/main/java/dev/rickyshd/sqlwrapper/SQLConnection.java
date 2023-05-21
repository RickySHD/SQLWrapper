package dev.rickyshd.sqlwrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SQLConnection {
    static @NotNull SQLConnection to(@NotNull String host, int port, @NotNull String username,
                                     @NotNull String password, @NotNull String database) {
        return new SQLConnectionImpl(host, port, username, password, database);
    }

    boolean connect();

    boolean testConnect();

    Optional<List<Object>> executeQueryGetValue(@NotNull String query, @NotNull String valueName, Object... arguments);

    Optional<Map<String, List<Object>>> executeQueryByColumn(@NotNull String query, Object... arguments);

    Optional<List<Map<String, Object>>> executeQueryByRow(@NotNull String query, Object... arguments);

    boolean executeStatement(@NotNull String statement, Object... arguments);

    boolean executeFromFile(@NotNull String path);
}
