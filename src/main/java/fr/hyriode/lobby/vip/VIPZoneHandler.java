package fr.hyriode.lobby.vip;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VIPZoneHandler extends HyriListener<HyriLobby> {

    public VIPZoneHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Area vip = this.plugin.config().getVIPZone().asArea();
        final Area pvp = this.plugin.config().getPvpZone().asArea();
        final Player player = e.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());
        final IHyriPlayer hyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());


        if(vip.isInArea(player.getLocation())) {
            if(hyriPlayer.getRank().is(HyriPlayerRankType.PLAYER)) {
                player.teleport(this.plugin.config().getVIPLocation().asBukkit());
            }
        }

        if (!lobbyPlayer.isInPvp()) {
            if (pvp.isInArea(player.getLocation())) {
                lobbyPlayer.setInPvP(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        event.setCancelled(true);

        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            final LobbyPlayer lp = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if (lp.isInPvp()) {
                if(event.getCause() != EntityDamageEvent.DamageCause.FALL) {

                    event.setCancelled(false);

                    if (player.getHealth() - event.getFinalDamage() <= 0.0D) {
                        event.setDamage(0.0D);
                        event.setCancelled(true);

                        lp.handleLogin(false, false);
                        this.teleport(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if(lobbyPlayer.isInPvp()) {
                if (player.getHealth() - event.getFinalDamage() <= 0.0D) {
                    ((Player) event.getDamager()).setHealth(((Player) event.getDamager()).getHealth() + 5);

                    event.setDamage(0.0D);
                    event.setCancelled(true);

                    lobbyPlayer.handleLogin(false, false);
                    this.teleport(player);
                }
            }
        }
    }

    private void teleport(final Player player) {
        player.teleport(this.plugin.config().getPvpLocation().asBukkit());
        player.getInventory().setHeldItemSlot(2);
    }
}