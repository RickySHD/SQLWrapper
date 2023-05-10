package dev.rickyshd.sqlwrapper;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface SQLConnection {
    static @NotNull SQLConnection to(@NotNull String host, int port, @NotNull String username,
                                     @NotNull String password, @NotNull String database) {
        return new SQLConnectionImpl(host, port, username, password, database);
    }

    boolean connect();

    boolean testConnect();

    public Optional<List<Object>> executeQuery(@NotNull String query, @NotNull String valueName, Object... arguments);

    public Optional<ResultSet> executeQueryRaw(@NotNull String query, Object... arguments);

    public boolean executeStatement(@NotNull String statement, Object... arguments);

    public boolean executeFromFile(@NotNull String path);
}
