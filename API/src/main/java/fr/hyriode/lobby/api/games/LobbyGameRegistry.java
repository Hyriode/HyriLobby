package fr.hyriode.lobby.api.games;

import fr.hyriode.lobby.api.redis.LobbyDataManager;
import fr.hyriode.lobby.api.redis.RedisKey;

public class LobbyGameRegistry extends LobbyDataManager<LobbyGame> {

    public LobbyGameRegistry() {
        super(RedisKey.GAMES.getKey(), LobbyGame.class);
    }

    public void registerGame(LobbyGame game) {
        final LobbyGame registeredGame = this.get(game.getName());

        if (registeredGame == null) {
            this.save(game, game.getName());
            return;
        }

        if (registeredGame.hasType(game.getTypes().get(0))) {
            return;
        }

        registeredGame.addType(game.getTypes().get(0));
        this.save(registeredGame, registeredGame.getName());
    }

    public void unregisterGame(String game, String type) {
        final LobbyGame registeredGame = this.get(game);

        if (registeredGame == null) {
            return;
        }

        if (!registeredGame.hasType(type)) {
            return;
        }

        registeredGame.removeType(type);
        this.save(registeredGame, registeredGame.getName());
    }
}
