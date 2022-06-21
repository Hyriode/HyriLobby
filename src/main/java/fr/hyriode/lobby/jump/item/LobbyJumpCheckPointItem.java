package fr.hyriode.lobby.jump.item;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 03/05/2022 at 20:16
 */
public class LobbyJumpCheckPointItem extends HyriItem<HyriLobby> {

    public LobbyJumpCheckPointItem(HyriLobby plugin) {
        super(plugin, "jump_checkpoint", () -> HyriLanguageMessage.get("item.jump-checkpoint.name"), Material.SLIME_BALL);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());

        if(lobbyPlayer.hasJump()) {
            if(lobbyPlayer.getJump().getActualCheckPoint() != null) {
                lobbyPlayer.asPlayer().teleport(lobbyPlayer.getJump().getActualCheckPoint().getLocation());
                lobbyPlayer.asPlayer().sendMessage(LobbyMessage.JUMP_GO_BACK_CHECKPOINT.asLang().getForPlayer(lobbyPlayer.asPlayer()));
            }
        }
    }
}
