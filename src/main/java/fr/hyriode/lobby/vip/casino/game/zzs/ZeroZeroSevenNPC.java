package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.npc.SingleNPCHandler;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ZeroZeroSevenNPC extends SingleNPCHandler {

    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -369D, 163D, -21D, -100F, 0F);
    public ZeroZeroSevenNPC(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(this.npcLocation, UsefulSkin.ZZS_SKIN, Collections.singletonList("007"))
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((b, player1) -> {
                    if(!b) return;
                    new ZeroZeroSevenGame(player1).getInventory().open();
                });
    }
}
