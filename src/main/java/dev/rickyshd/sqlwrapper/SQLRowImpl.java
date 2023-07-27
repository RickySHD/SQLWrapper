package dev.rickyshd.sqlwrapper;

import java.util.Map;
import java.util.Set;

final class SQLRowImpl implements SQLRow {
    private final Map<String, Object> content;

    public SQLRowImpl(Map<String, Object> _content) {
        content = _content;
    }

    @Override
    public boolean isNotEmpty() {
        return !content.isEmpty();
    }

    @Override
    public Set<String> getLabels() {
        return content.keySet();
    }

    @Override
    public Object getValue(String columnLabel) {
        if (!content.containsKey(columnLabel)) return null;

        return content.get(columnLabel);
    }

    @Override
    public <T> T getValue(String columnLabel, Class<T> type) {
        if (!content.containsKey(columnLabel)) return null;

        return type.cast(content.get(columnLabel));
    }
}
