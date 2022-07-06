package fr.hyriode.lobby.npc;

import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:44
 */
public abstract class SingleNPCHandler extends LobbyNPCHandler {

    protected final Map<UUID, NPC> npcs;

    public SingleNPCHandler(HyriLobby plugin) {
        super(plugin);
        this.npcs = new HashMap<>();
    }

    @Override
    public void disable() {
        for (NPC npc : this.npcs.values()) {
            NPCManager.removeNPC(npc);
        }
    }

    @Override
    public void onLogin(Player player) {
        final NPC npc = this.createNPC(player);

        if (npc == null) {
            return;
        }

        NPCManager.sendNPC(player, npc);

        this.npcs.put(player.getUniqueId(), npc);
    }

    @Override
    public void onLogout(Player player) {
        final NPC npc = this.npcs.remove(player.getUniqueId());

        if (npc == null) {
            return;
        }

        NPCManager.removeNPC(npc);
    }

    public abstract NPC createNPC(Player player);

}
