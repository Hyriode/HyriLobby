package fr.hyriode.hyrilobby.vip;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VipZoneHandler extends HyriListener<HyriLobby> {

    public VipZoneHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Area vip = this.plugin.getConfiguration().getVipEntry().asArea();
        final Area pvp = this.plugin.getConfiguration().getPvpZone().asArea();
        final Player player = e.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());
        final IHyriPlayer hyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());

        if(vip.isInArea(player.getLocation())) {
            if(hyriPlayer.getRank().is(HyriPlayerRankType.PLAYER)) {
                player.teleport(this.plugin.getConfiguration().getVipLocation().asBukkit());
            }
        }

        if(pvp.isInArea(player.getLocation())) {
            lobbyPlayer.setInPvP();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if (player.getHealth() - event.getFinalDamage() <= 0.0D) {
                event.setDamage(0.0D);
                event.setCancelled(true);

                lobbyPlayer.handleLogin(false);
            }
        }
    }
}