package fr.hyriode.lobby.npc.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.selector.game.RotatingGameTypeSelectorGUI;
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
                npc.getHologram().updateLines();
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

        final NPC npc = NPCManager.createNPC(this.npcLocation, UsefulSkin.DICE_SKIN, header)
                .setTrackingPlayer(false)
                .setShowingToAll(false)
                .setInteractCallback((rightClick, target) -> {
                    if (rightClick) {
                        final IHyriRotatingGame game = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();

                        if (game == null || game.getInfo() == null) {
                            return;
                        }

                        new RotatingGameTypeSelectorGUI(this.plugin, target, false).open();
                    }
                });
        final Hologram hologram = npc.getHologram();

        for (int i = 0; i < header.size(); i++) {
            final int index = i;

            hologram.setLine(index, new Hologram.Line(target -> header.get(index)));
        }

        hologram.setLine(header.size(), new Hologram.Line(new NPCPlayLine()));

        npc.addPlayer(player);

        return npc;
    }

}
