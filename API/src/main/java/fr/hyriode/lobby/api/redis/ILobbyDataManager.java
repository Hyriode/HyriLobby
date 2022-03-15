package fr.hyriode.lobby.api.redis;

import java.util.Set;

public interface ILobbyDataManager<D extends ILobbyData, K> {

    D get(K key);

    void save(D data);
    void delete(D data);

    Set<String> getAllKeys();
    Set<D> getAllKeysAsValues();
}
