package fr.hyriode.lobby.npc.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.npc.SingleNPCHandler;
import fr.hyriode.lobby.npc.util.NPCPlayLine;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:43
 */
public class RotatingGameNPCHandler extends SingleNPCHandler {

    private final BukkitTask refreshTask;

    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -45.5, 187, 0.5, -90, 0);

    public RotatingGameNPCHandler(HyriLobby plugin) {
        super(plugin);

        this.refreshTask = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (NPC npc : this.npcs.values()) {
                npc.getHologram().updateLine(3);
            }
        }, 2L, 2L);
    }

    @Override
    public void disable() {
        super.disable();

        this.refreshTask.cancel();
    }

    @Override
    public NPC createNPC(Player player) {
        final List<String> header = LobbyMessage.NPC_ROTATING_GAME_HEADER.asList(player);

        header.add(" ");

        final NPC npc = NPCManager.createNPC(this.npcLocation, UsefulSkin.DICE_SKIN, header)
                .setTrackingPlayer(false)
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((rightClick, target) -> {});

        npc.getHologram().setLine(3, new Hologram.Line(new NPCPlayLine()));

        return npc;
    }

}
