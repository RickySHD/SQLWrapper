package dev.rickyshd.sqlwrapper;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.NotYetConnectedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class SQLConnectionImpl implements SQLConnection {
    private final MysqlDataSource source;
    private Connection conn;

    public SQLConnectionImpl(String host, int port, String username, String password, String database) {
        this.source = new MysqlConnectionPoolDataSource();
        source.setServerName(host);
        source.setPort(port);
        source.setUser(username);
        source.setPassword(password);
        source.setDatabaseName(database);
    }

    @Override
    public boolean connect() {
        try {
            if (conn != null && conn.isValid(1)) return true;

            conn = source.getConnection();
        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    @Override
    public boolean testConnect() {
        try (Connection conn = source.getConnection()) {
            if (!conn.isValid(1)) return false;
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    @Override
    public Optional<List<Object>> executeQuery(@NotNull String query, @NotNull String valueName, Object... arguments) {
        try {
            Optional<ResultSet> _set = executeQueryRaw(query, arguments);
            if (_set.isEmpty()) return Optional.empty();

            ResultSet set = _set.get();
            List<Object> results = new ArrayList<>();
            while (set.next())
                results.add(set.getObject(valueName));

            return Optional.of(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ResultSet> executeQueryRaw(@NotNull String query, Object... arguments) {
        if (conn == null) throw new NotYetConnectedException();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            int i = 0;
            for (Object arg : arguments)
                stmt.setObject(i++, arg);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return Optional.of(resultSet);

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean executeStatement(@NotNull String statement, Object... arguments) {
        if (conn == null) throw new NotYetConnectedException();

        try (PreparedStatement stmt = conn.prepareStatement(statement)) {

            int i = 0;
            for (Object arg : arguments)
                stmt.setObject(i++, arg);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean executeFromFile(@NotNull String path) {
        File file = new File(path);
        String queries;

        try (InputStream in = new FileInputStream(file)) {
            queries = new String(in.readAllBytes());
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean ret = true;
        for (String query : queries.split(";"))
            ret &= executeStatement(query);

        return ret;
    }
}
