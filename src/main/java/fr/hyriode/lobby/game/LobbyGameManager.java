package fr.hyriode.lobby.game;

import fr.hyriode.hyrame.HyrameLogger;
import fr.hyriode.lobby.game.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 23/06/2022 at 11:45
 */
public class LobbyGameManager {

    private final Map<String, LobbyGame> games;

    public LobbyGameManager() {
        this.games = new LinkedHashMap<>();

        this.registerGame(new PearlControlGame());
        this.registerGame(new RushTheFlagGame());
        this.registerGame(new TheRunnerGame());
        this.registerGame(new BridgerGame());
        this.registerGame(new BedWarsGame());
        this.registerGame(new LaserGame());
        this.registerGame(new GetDownGame());
        this.registerGame(new SheepWarsGame());
    }

    public <T extends LobbyGame> T registerGame(T game) {
        final String gameName = game.getName();

        if (!this.games.containsKey(gameName)) {
            this.games.put(gameName, game);

            HyrameLogger.log("Registered '" + gameName + "' lobby game.");

            return game;
        }
        return null;
    }

    public List<LobbyGame> getGames() {
        return new ArrayList<>(this.games.values());
    }

    public List<LobbyGame> getGamesInSelector() {
        final List<LobbyGame> games = new ArrayList<>();

        for (LobbyGame game : this.games.values()) {
            if (game.isUsedInSelector()) {
                games.add(game);
            }
        }
        return games;
    }

}
