package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.games.LobbyGame;
import fr.hyriode.lobby.gui.utils.GameItem;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GameTypeChooser extends LobbyInventory {

    private static final List<Integer> DONT_FILL = Arrays.asList(19, 19 + 9, 25, 25 + 9);

    private final LobbyGame game;
    private final ItemBuilder builder;

    public GameTypeChooser(HyriLobby plugin, Player owner, LobbyGame game) {
        super(owner, plugin, "game_type_selector", game.getTypes().size() > 5 ? 54 : 45);

        this.game = game;
        this.builder = GameItem.getByName(game.getName()).getItem();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(0, new ItemBuilder(Material.COMPASS).withName(this.getMessage("back")).build(),
                e -> new GamesChooserGui(this.plugin, this.owner).open()
        );
        this.setItem(4, this.builder.clone().withName(this.game.getDisplayName()).withLore(this.getMessage("lobby_selector", "connected") + this.getPlayersCount()).build());

        final int[] slot = {19};
        this.game.getTypes().forEach(type -> {
            //TODO Display name for game type, to avoid "1_1" or "SQUAD" in lore

        });
    }

    private int getPlayersCount() {
        return HyriAPI.get().getServerManager().getServers(this.game.getName()).stream().mapToInt(server -> server.getPlayers().size()).sum();
    }
}
