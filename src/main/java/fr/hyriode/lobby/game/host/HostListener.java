package fr.hyriode.lobby.game.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.host.HostAdvertisementEvent;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.lobby.language.LobbyMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 26/10/2022 at 21:49
 */
public class HostListener {

    public HostListener() {
        HyriAPI.get().getNetworkManager().getEventBus().register(this);
    }

    @HyriEventHandler
    public void onAdvert(HostAdvertisementEvent event) {
        final HostData hostData = event.getHostData().orElse(null);

        if (hostData == null) {
            return;
        }

        final HyggServer server = HyriAPI.get().getServerManager().getServer(event.getServerName());
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(server.getType());
        final HostState state = HostState.getFromServer(server);

        if (state == null) {
            return;
        }

        final String owner = IHyriPlayer.get(hostData.getOwner()).getNameWithRank();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final String[] message = LobbyMessage.HOST_ADVERT_MESSAGE.asString(player).split("%split%");
            final String hover = ChatColor.AQUA + hostData.getName() + "\n" +
                    LobbyMessage.HOST_ITEM_LORE.asString(player).replace("%owner%", owner)
                        .replace("%game%", gameInfo.getDisplayName())
                        .replace("%game_type%", gameInfo.getType(server.getGameType()).getDisplayName())
                        .replace("%state%", state.getDisplay().getValue(player))
                        .replace("%players%", String.valueOf(server.getPlayingPlayers().size()))
                        .replace("%slots%", String.valueOf(server.getSlots())) +
                    "\n\n" +
                    LobbyMessage.CLICK_TO_JOIN.asString(player);
            final ComponentBuilder builder = new ComponentBuilder(message[0].replace("%player%", owner))
                    .append(" ")
                    .append(message[1]).color(net.md_5.bungee.api.ChatColor.AQUA).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hostinvitation " + event.getServerName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));

            player.spigot().sendMessage(builder.create());
        }

    }

}
