package fr.hyriode.lobby.api.utils;

import java.util.HashMap;
import java.util.Map;

public class HyriReverseMap<K, V> extends HashMap<K, V> {

    private final Map<V, K> reverse;

    public HyriReverseMap() {
        this.reverse = new HashMap<>();
    }

    @Override
    public V put(K key, V value) {
        this.reverse.put(value, key);
        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {
        this.reverse.remove(super.get(key));
        return super.remove(key);
    }

    public K getReverse(V key) {
        return this.reverse.get(key);
    }
}
