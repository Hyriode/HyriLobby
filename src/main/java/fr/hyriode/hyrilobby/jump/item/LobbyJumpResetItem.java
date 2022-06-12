package fr.hyriode.hyrilobby.jump.item;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

/**
 * Project: Hyriode-Development
 * Created by Akkashi
 * on 09/06/2022 at 18:06
 */
public class LobbyJumpResetItem extends HyriItem<HyriLobby> {

    public LobbyJumpResetItem(HyriLobby plugin) {
        super(plugin, "jump_reset", () -> HyriLanguageMessage.get("item.jump-reset.name"), Material.LEASH);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());

        if(lobbyPlayer.hasJump()) {
            lobbyPlayer.resetJump();
        }
    }
}
