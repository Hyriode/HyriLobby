package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.SingleNPCHandler;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ZeroZeroSevenNPC extends SingleNPCHandler {

    private final ZeroZeroSevenGame game;
    public ZeroZeroSevenNPC(HyriLobby plugin) {
        super(plugin);
        this.game = new ZeroZeroSevenGame();
    }

    @Override
    public NPC createNPC(Player player) {
        return NPCManager.createNPC(new Location(IHyrame.WORLD.get(), -369D, 163D, -21D, -100F, 0F), UsefulSkin.EARTH_SKIN, Collections.singletonList("007"))
                .setInteractCallback((b, player1) -> {
                    if(!b) return;
                    //TODO MAKE THE GAME
                });
    }
}
