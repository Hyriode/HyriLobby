package fr.hyriode.lobby.npc.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.npc.SingleNPCHandler;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:24
 */
public class LanguageNPCHandler extends SingleNPCHandler {

    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -2.5, 190, -3.5, -37, 0);

    public LanguageNPCHandler(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(this.npcLocation, UsefulSkin.EARTH_SKIN, LobbyMessage.NPC_LANGUAGE_HEADER.asList(player))
                .setTrackingPlayer(false)
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((rightClick, target) -> new LanguageGUI(this.plugin, target).open());
    }

}
