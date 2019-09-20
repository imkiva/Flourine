package com.imkiva.flourine.script.runtime.types;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class ListValue implements Iterable<Value>, Collection<Value> {
    private List<Value> items;

    public ListValue(List<Value> items) {
        this.items = items;
    }

    public Value getItem(int i) {
        return items.get(i);
    }

    public List<Value> getItems() {
        return items;
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

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }

    @Override
    public Iterator<Value> iterator() {
        return items.iterator();
    }

    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return items.toArray(a);
    }

    @Override
    public boolean add(Value value) {
        return items.add(value);
    }

    @Override
    public boolean remove(Object o) {
        return items.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Value> c) {
        return items.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return items.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return items.retainAll(c);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void forEach(Consumer<? super Value> action) {
        items.forEach(action);
    }

    @Override
    public Spliterator<Value> spliterator() {
        return items.spliterator();
    }
}
