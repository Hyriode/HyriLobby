package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.api.util.Skin;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.npc.SingleNPCHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class ZeroZeroSevenNPC extends SingleNPCHandler {

    private final Location npcLocation = new Location(IHyrame.WORLD.get(), -369D, 163D, -21D, -100F, 0F);
    public ZeroZeroSevenNPC(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    public NPC createNPC(Player player) {
        final Skin skin = new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY3NzA5NDE0MzU0OSwKICAicHJvZmlsZUlkIiA6ICI1MTY4ZjZlMjIyM2E0Y2FjYjdiN2QyZjYyZWMxZGFhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJkZWZfbm90X2FzaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85OWJiNjZiMmY0NDk5NjdjZTY0MzNjOGE4MzNkOTIwMzcyNjYzZTRmY2M4OTEzNDcwYWY3YWZmMzgyNzM4MDMiCiAgICB9CiAgfQp9", "Ba7HH23FrtHweSXMwBj1Y/9/j6QMjhdj3d8CGv4CzaIAUAXKZVpSS44sy/tIoq37bo34IYrSXa7vqVW2d+L7zPPOMhhWsuHGy+6KdKetb02xJkv8K+8Z1ix1XEniv2DsEuhjs2XkiGk52vPtnhT7cUoWrsjh6MOpc6mO9fCRmR5RiOS1b6W+4TTvhd7nA120N+zgdl8IJCb+4NkBa4dpxHvDxQbDSCnXsHi1U9/8w2UpHPC/Rh1SofMzvMcR1p9jILXSX5LrIIVlcK6VuyZYERBARaOj9ANN+rUY+79a2snc1XlD8lVKZkcnLILoKUMu1wj1GkWmsmL+SZSMu/J0YHrOopFD2wXXaO2l8MzE29sdHjgi76vJ450O/6Suqa7PMiHgvAh1BrnuG1Oe5aB1k5qwraXnuRtFLE7zGesGu6R3BiSi0opffm7I1DqZmkWPzM02yMCqlyqTvcj/18s2JFcqCn/1y/Xe2Zo5LrgMNqICPzBOYe1qNe8ijE+Dv6NN1iDSQgfsO2ZNspmfB6qz9bclindRxqFjlqMngdyfMnOi8bpyegoiss2Y41GRU9kl2rPtbqfbZyzjEeRxhPwoWqwpTTDMt5BmuUFdVJNr49fwy5x8jTD+BAISbuFyrPUYIUYZ5Ahs0dih1uB7sxRdXyAxTqenUfsXt3Ub3vo556w=");
        return NPCManager.createNPC(this.npcLocation, skin, Collections.singletonList("007"))
                .setShowingToAll(false)
                .addPlayer(player)
                .setInteractCallback((b, player1) -> {
                    if(!b) return;
                    new ZeroZeroSevenGame(player1).getInventory().open();
                });
    }
}
