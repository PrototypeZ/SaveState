package io.github.prototypez.savestate.entity;

public class User<T> {
    String name;
    T something;

    public User(){

    }

    public User(String name, T something) {
        this.name = name;
        this.something = something;
    }

    @Override
    public String toString() {
        return "{name="+name + ", something=" + something + "}";
    }
}
