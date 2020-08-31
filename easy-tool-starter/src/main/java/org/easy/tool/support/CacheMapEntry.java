package org.easy.tool.support;

public class CacheMapEntry<K,V> {


    long time;
    V value;
    K key;

    CacheMapEntry(K key, V value) {
        super();
        this.value = value;
        this.key = key;
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        return this.value = value;
    }
}
