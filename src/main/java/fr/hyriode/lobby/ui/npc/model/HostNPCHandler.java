package fr.hyriode.lobby.ui.npc.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.host.HostGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.ui.npc.SingleNPCHandler;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:43
 */
public class HostNPCHandler extends SingleNPCHandler {

    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -37.5, 187, -5.5, -45, 0);

    public HostNPCHandler(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(this.npcLocation, UsefulSkin.HOST_SKIN, LobbyMessage.NPC_HOST_HEADER.asList(player).toArray(new String[0]))
                .setTrackingPlayer(false)
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((rightClick, target) -> {
                    if (rightClick) {
                        new HostGUI(target, this.plugin, true).open();
                    }
                });
    }

}
