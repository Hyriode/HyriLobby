package fr.hyriode.lobby.api.redis;

import java.util.Set;

/**
 * Represents a lobby data manager, which is used to store and retrieve data in Redis.
 * @param <D> The type of data to store.
 * @param <K> The type of key to use, usually a String representing the data's name.
 */
public interface ILobbyDataManager<D extends ILobbyData, K> {

    D get(K key);

    void save(D data);
    void delete(D data);

    Set<String> getAllKeys();
    Set<D> getAllKeysAsValues();
}
