package fr.hyriode.hyrilobby.jump.item;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.function.Supplier;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 03/05/2022 at 20:16
 */
public class LobbyJumpLeaveItem extends HyriItem<HyriLobby> {

    public LobbyJumpLeaveItem(HyriLobby plugin) {
        super(plugin, "jump_leave", () -> HyriLanguageMessage.get("item.jump-leave.name"), Material.BED);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());

        if(lobbyPlayer.hasJump()) {
            lobbyPlayer.leaveJump(false);
        }
    }
}
