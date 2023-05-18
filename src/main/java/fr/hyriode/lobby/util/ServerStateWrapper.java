package fr.hyriode.lobby.util;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static fr.hyriode.hyggdrasil.api.server.HyggServer.State;

/**
 * Project: Hyriode
 * Created by AstFaster
 * on 20/05/2022 at 22:16
 */
public enum ServerStateWrapper {

    READY(2, State.READY, UsefulHead.LIME_GREEN_CUBE, "ready"),
    PLAYING(3, State.PLAYING, UsefulHead.RED_CUBE, "playing"),

    ;

    private static final Map<State, ServerStateWrapper> BY_INITIAL = new HashMap<>();

    static {
        for (ServerStateWrapper state : values()) {
            BY_INITIAL.put(state.getInitial(), state);
        }
    }

    private final int id;
    private final State initial;
    private final ItemStack item;
    private final HyriLanguageMessage displayName;

    ServerStateWrapper(int id, State initial, ItemStack item, String name) {
        this.id = id;
        this.initial = initial;
        this.item = item;
        this.displayName = HyriLanguageMessage.get("state." + name + ".display");
    }

    ServerStateWrapper(int id, State initial, UsefulHead head, String name) {
        this(id, initial, ItemBuilder.asHead(head).build(), name);
    }

    public int getId() {
        return this.id;
    }

    public State getInitial() {
        return this.initial;
    }

    public ItemStack getItem() {
        return this.item.clone();
    }

    public HyriLanguageMessage getDisplayName() {
        return this.displayName;
    }

    public static ServerStateWrapper from(State serverState) {
        return BY_INITIAL.get(serverState);
    }

}
