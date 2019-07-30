package com.imkiva.flourine.script.runtime.types;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class ListValue {
    private List<Value> items;

    public ListValue(List<Value> items) {
        this.items = items;
    }

    public Value getItem(int i) {
        return items.get(i);
    }

    public int getSize() {
        return items.size();
    }

    @Override
    public String toString() {
        return "{" +
                items.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListValue listValue = (ListValue) o;
        return Objects.equals(items, listValue.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
