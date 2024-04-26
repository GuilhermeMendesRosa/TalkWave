package br.udesc.model;

import java.util.Objects;

public class Key {
    private final User first;
    private final User second;

    public Key(User first, User second) {
        this.first = first;
        this.second = second;
    }

    public User getFirst() {
        return first;
    }

    public User getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key that = (Key) o;
        return (Objects.equals(first, that.first) && Objects.equals(second, that.second)) ||
                (Objects.equals(first, that.second) && Objects.equals(second, that.first));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second) + Objects.hash(second, first);
    }
}
