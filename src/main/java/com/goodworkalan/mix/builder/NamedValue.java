package com.goodworkalan.mix.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.List;

/**
 * A name value pair.
 * 
 * @author Alan Gutierrez
 */
public class NamedValue {
    /** The value name. */
    private final String name;

    /** The value. */
    private final Object value;

    /**
     * Create a named value with the given name and the given value.
     * 
     * @param name
     *            The value name.
     * @param value
     *            The value.
     */
    public NamedValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the value name.
     * 
     * @return The value name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Convert the given list of named values to a map, the last value specified
     * wins.
     * 
     * @return The list of named values as map.
     */
    public Map<String, Object> asMap(List<NamedValue> namedValues) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (NamedValue namedValue : namedValues) {
            map.put(namedValue.getName(), namedValue.getValue());
        }
        return map;
    }

    /**
     * Convert the given list of named values to a multi-map, the values are
     * stored in a list.
     * 
     * @return The list of named values as multi-map.
     */
    public Map<String, List<Object>> asMultiMap(List<NamedValue> namedValues) {
        Map<String, List<Object>> map = new LinkedHashMap<String, List<Object>>();
        for (NamedValue namedValue : namedValues) {
            List<Object> values = map.get(namedValue.getName());
            if (values == null) {
                values = new ArrayList<Object>();
                map.put(namedValue.getName(), values);
            }
            values.add(namedValue.getValue());
        }
        return map;
    }
}
