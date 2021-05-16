package com.coursework.models;

import java.util.*;

public class Token {
    private final String value;
    private final HashMap<String, List<Integer>> matches;

    public Token(){
        this(null);
    }

    public Token(String value) {
        this.value = value;
        this.matches = new HashMap<>();
    }

    public String value() {
        return value;
    }

    public HashMap<String, List<Integer>> matches() {
        return this.matches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", matches=" + matches +
                '}';
    }
}
