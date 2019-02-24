package com.redsponge.energygame.utils;

import com.badlogic.gdx.utils.DelayedRemovalArray;

import java.util.HashMap;
import java.util.function.Consumer;

public class HashCollections<T> {

    private HashMap<Class<? extends T>, DelayedRemovalArray<T>> objects;

    public HashCollections() {
        objects = new HashMap<Class<? extends T>, DelayedRemovalArray<T>>();
    }

    public void addType(Class<? extends T> type) {
        objects.put(type, new DelayedRemovalArray<T>());
    }

    public void add(T obj) {
        objects.get(obj.getClass()).add(obj);
    }

    public void clear(Class<? extends T> type) {
        DelayedRemovalArray<T> arr = objects.get(type);
        if(arr == null) {
            throw new RuntimeException("Couldn't clear array of type [" + objects.getClass().getName() + "] as it doesn't exist!");
        }
        arr.clear();
    }

    public void clearAll() {
        for(DelayedRemovalArray<T> arr : objects.values()) {
            arr.clear();
        }
    }

    public <K extends T> DelayedRemovalArray<K> get(Class<K> type) {
        DelayedRemovalArray<K> arr = (DelayedRemovalArray<K>) objects.get(type);
        if(arr == null) {
            throw new RuntimeException("Couldn't get array of type [" + objects.getClass().getName() + "] as it doesn't exist!");
        }
        return arr;
    }

    public void forEach(Consumer<T> method) {
        for(DelayedRemovalArray<T> arr : objects.values()) {
            arr.forEach(method);
        }
    }

    public void remove(T e) {
        objects.get(e.getClass()).removeValue(e, true);
    }
}
