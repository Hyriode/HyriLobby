package fr.hyriode.lobby;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGame;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class TestGame extends HyriGame<TestGamePlayer> {

    public TestGame(IHyrame hyrame, JavaPlugin plugin) {
        super(hyrame, plugin, "test-" + UUID.randomUUID().toString().split("-")[0], "Game Test-" + UUID.randomUUID().toString().split("-")[0], TestGamePlayer.class, true);

        this.minPlayers = 2;
        this.maxPlayers = 10;
    }

    @Override
    public void start() {
        super.start();
    }
}
