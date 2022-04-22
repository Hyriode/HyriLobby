package fr.hyriode.lobby.games;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.hyrame.game.event.HyriGameRegisteredEvent;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.games.LobbyGame;

public class LobbyGameRegistryListener {

    @HyriEventHandler
    public void onGameRegistered(HyriGameRegisteredEvent event) {
        final LobbyGame game = new LobbyGame(event.getGame().getName(), event.getGame().getDisplayName(), event.getGame().getType().getName());

        LobbyAPI.get().getGameRegistry().registerGame(game);
    }
}
