package fr.hyriode.lobby.vip.casino;

import fr.hyriode.lobby.vip.casino.game.IGame;
import fr.hyriode.lobby.vip.casino.game.RollerMachinesGame;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private final Map<Material, IGame> blockMap = new HashMap<>();

    public GameManager() {
        this.blockMap.put(Material.LEVER, new RollerMachinesGame());
    }
    public Map<Material, IGame> getBlockMap() {
        return blockMap;
    }
}
