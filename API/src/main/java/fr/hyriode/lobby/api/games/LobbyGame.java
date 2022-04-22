package fr.hyriode.lobby.api.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LobbyGame {

    private final String name;
    private final String displayName;
    private final List<String> types;

    public LobbyGame(String name, String displayName, List<String> types) {
        this.name = name;
        this.displayName = displayName;
        this.types = types;
    }

    public LobbyGame(String name, String displayName, String type) {
        this.name = name;
        this.displayName = displayName;
        this.types = Collections.singletonList(type);
    }

    public LobbyGame(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.types = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getTypes() {
        return this.types;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public boolean hasType(String type) {
        return this.types.contains(type);
    }

    public void removeType(String type) {
        this.types.remove(type);
    }
}
