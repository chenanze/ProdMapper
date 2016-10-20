package com.chenanze.prodmapper.utils;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by duian on 2016/10/17.
 */

public class LinkedListStack<T> {

    private LinkedList<T> storage = new LinkedList<>();

    public void push(T v) {
        storage.addFirst(v);
    }

    public T peek() {
        return storage.getFirst();
    }

    public T pop() {
        return storage.removeFirst();
    }

    public boolean empty() {
        return storage.isEmpty();
    }

    public String toString() {
        return storage.toString();
    }

    public int size() {
        return storage.size();
    }

    public ListIterator<T> listIterator() {
        return storage.listIterator();
    }

}

