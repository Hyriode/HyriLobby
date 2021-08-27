package fr.hyriode.hyrilobby.tab;

import fr.hyriode.common.tab.Tab;
import org.bukkit.entity.Player;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 27/08/2021 at 08:47
 */
public class TabManager {

    private final Tab tab;

    public TabManager() {
        this.tab = new LobbyTab();
    }

    public void onLogin(Player player) {
        this.tab.send(player);
    }

}
