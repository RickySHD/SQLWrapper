package dev.rickyshd.sqlwrapper;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.NotYetConnectedException;
import java.sql.*;
import java.util.*;

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
            return true;
        } catch (SQLException e) {
            return false;
        }
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

    @Deprecated
    @Override
    public Optional<Map<String, List<Object>>> executeQueryByColumn(@NotNull String query, Object... arguments) {
        if (conn == null) throw new NotYetConnectedException();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            int i = 1;
            for (Object arg : arguments)
                stmt.setObject(i++, arg);

            ResultSet resultSet = stmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            Map<String, List<Object>> result = new HashMap<>();

            for (int j=1; j <= metaData.getColumnCount(); j++)
                result.put(metaData.getColumnLabel(j), new ArrayList<>());

            while (resultSet.next())
                for (String label : result.keySet())
                    result.get(label).add(resultSet.getObject(label));

            return Optional.of(result);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SQLRow> executeQuery(@NotNull String query, Object... arguments) {
        if (conn == null) throw new NotYetConnectedException();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            int i = 1;
            for (Object arg : arguments)
                stmt.setObject(i++, arg);

            ResultSet resultSet = stmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<SQLRow> result = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            for (int j=1; j <= metaData.getColumnCount(); j++)
                labels.add(metaData.getColumnLabel(j));

            while (resultSet.next()) {
                Map<String, Object> m = new HashMap<>();
                for (String label : labels)
                    m.put(label, resultSet.getObject(label));

                result.add(SQLRow.from(m));
            }
            return result;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean executeStatement(@NotNull String statement, Object... arguments) {
        if (conn == null) throw new NotYetConnectedException();

        try (PreparedStatement stmt = conn.prepareStatement(statement)) {

            int i = 1;
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
