package fr.hyriode.lobby.quest;

import fr.hyriode.api.util.Skin;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.npc.SingleNPCHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;

public abstract class AQuest extends SingleNPCHandler {

    public AQuest(HyriLobby plugin) {
        super(plugin);
    }


    protected Player player;

    protected abstract String getNPCName();

    protected abstract Location getNPCLocation();

    protected abstract Skin getSkin();

    protected abstract String getLore();

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(this.getNPCLocation(), this.getSkin(), Collections.singletonList(this.getNPCName()))
                .setTrackingPlayer(true)
                .setShowingToAll(true)
                .addPlayer(player)
                .setInteractCallback((rightClick, player1) -> {
                    if (rightClick) {
                        player1.sendMessage(ChatColor.RED + "(Quête) " + this.getNPCName() + " : " + this.getLore());
                    }
                });
    }
}
