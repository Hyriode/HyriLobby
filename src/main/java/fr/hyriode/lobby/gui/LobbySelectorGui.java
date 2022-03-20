package fr.hyriode.lobby.gui;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LobbySelectorGui extends LobbyInventory {

    private final IHyriServerManager server;

    public LobbySelectorGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame(), "item.lobbySelector.", "item.lobbySelector.name",
                LobbyInventory.dynamicSize(HyriAPI.get().getServerManager().getLobbies().size() + 9));

        this.server = HyriAPI.get().getServerManager();

        this.init();
    }

    @Override
    protected void init() {
        //Items part
        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture()).withName(this.getKey("name")).build());

        int[] slot = {8};
        //Items with Consumer part
        this.server.getLobbies().forEach(lobby -> {
            this.setItem(slot[0] += 1, new ItemBuilder(Material.NETHER_STAR).withName(lobby.getName()).withLore(this.getKey("connected") + lobby.getPlayers()).build(),
                    e -> this.server.sendPlayerToServer(this.getOwner().getUniqueId(), lobby.getName()));
        });

        for (int i = 0; i <= 8; i++) {
            if (i != 4) {
                this.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).withName(" ").build());
            }
        }

        this.setFill(FILL_ITEM);
    }
}
