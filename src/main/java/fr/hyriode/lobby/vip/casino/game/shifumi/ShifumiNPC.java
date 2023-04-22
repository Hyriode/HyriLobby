package fr.hyriode.lobby.vip.casino.game.shifumi;

import fr.hyriode.api.util.Skin;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.SingleNPCHandler;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ShifumiNPC extends SingleNPCHandler {
    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -369D, 163D, -21D, -100F, 0F);
    public ShifumiNPC(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(this.npcLocation, UsefulSkin.SHIFUMU_SKIN, Collections.singletonList("Shifumi"))
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((b, player1) -> {
                    if (!b) return;
                    new ShifumiGame(player1).getInventory().open();
                });
    }
}
