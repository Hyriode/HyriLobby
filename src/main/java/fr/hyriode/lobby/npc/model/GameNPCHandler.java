package fr.hyriode.lobby.npc.model;

import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.gui.selector.game.GameTypeSelectorGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.npc.LobbyNPCHandler;
import fr.hyriode.lobby.npc.util.NPCPlayLine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:02
 */
public class GameNPCHandler extends LobbyNPCHandler {

    private final Map<UUID, List<NPC>> npcs;

    private final BukkitTask refreshTask;

    public GameNPCHandler(HyriLobby plugin) {
        super(plugin);
        this.npcs = new HashMap<>();

        this.refreshTask = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (List<NPC> npcs : this.npcs.values()) {
                for (NPC npc : npcs) {
                    npc.getHologram().updateLine(1);
                    npc.getHologram().updateLine(2);
                }
            }
        }, 2L, 2L);
    }

    @Override
    public void disable() {
        this.refreshTask.cancel();

        for (List<NPC> npcs : this.npcs.values()) {
            for (NPC npc : npcs) {
                NPCManager.removeNPC(npc);
            }
        }
    }

    @Override
    public void onLogin(Player player) {
        final UUID playerId = player.getUniqueId();
        final List<NPC> npcs = this.npcs.getOrDefault(playerId, new ArrayList<>());

        for (LobbyGame game : this.plugin.getGameManager().getGames()) {
            final NPC npc = this.createGameNPC(player, game);

            if (npc == null) {
                continue;
            }

            npcs.add(npc);

            NPCManager.sendNPC(player, npc);
        }

        this.npcs.put(playerId, npcs);
    }

    @Override
    public void onLogout(Player player) {
        final List<NPC> npcs = this.npcs.remove(player.getUniqueId());

        if (npcs == null) {
            return;
        }

        for (NPC npc : npcs) {
            NPCManager.removeNPC(npc);
        }
    }

    private NPC createGameNPC(Player player, LobbyGame game) {
        final LobbyGame.NPCData npcData = game.getNPCData();

        if (npcData == null) {
            return null;
        }

        final List<String> header = new ArrayList<>();
        final IHyriGameInfo gameInfo = game.getGameInfo();

        header.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + (gameInfo != null ? gameInfo.getDisplayName() : "Unknown"));
        header.add(" ");
        header.add(" ");

        final NPC npc = NPCManager.createNPC(npcData.getLocation(), npcData.getSkin(), header)
                .setTrackingPlayer(true)
                .setShowingToAll(false)
                .setInteractCallback((rightClick, target) -> {
                    if (rightClick) {
                        new GameTypeSelectorGUI(this.plugin, target, game, false).open();
                    }
                })
                .addPlayer(player);
        final Hologram hologram = npc.getHologram();

        hologram.setLine(1, new Hologram.Line(() -> LobbyMessage.GAME_NPC_HEADER_PLAYERS.asString(player).replace("%players%", String.valueOf(game.getPlayers()))));
        hologram.setLine(2, new Hologram.Line(new NPCPlayLine()));

        for (Map.Entry<EnumItemSlot, ItemStack> entry : npcData.getEquipment().entrySet()) {
            npc.setEquipment(entry.getKey(), entry.getValue());
        }

        return npc;
    }



}
