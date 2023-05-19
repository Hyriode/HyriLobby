package fr.hyriode.lobby.minigame.jump.item;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 03/05/2022 at 20:16
 */
public class LobbyJumpLeaveItem extends HyriItem<HyriLobby> {

    public LobbyJumpLeaveItem(HyriLobby plugin) {
        super(plugin, "jump_leave", () -> HyriLanguageMessage.get("item.jump-leave.name"), null, Material.BED);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());

        if(lobbyPlayer.hasJump()) {
            lobbyPlayer.leaveJump(true);
        }
    }
}
