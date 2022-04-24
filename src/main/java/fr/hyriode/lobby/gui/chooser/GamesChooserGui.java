package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.network.HyriNetworkCount;
import fr.hyriode.api.network.HyriPlayerCount;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.games.LobbyGame;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.Language;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamesChooserGui extends LobbyInventory {

    public GamesChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "games_selector", 54);

        this.init();
    }

    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        final ItemStack fill = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName("").build();

        this.setHorizontalLine(0, 8, fill);
        this.setHorizontalLine(45, 53, fill);
        this.setVerticalLine(9, 36, fill);
        this.setVerticalLine(17, 45, fill);

        this.setItem(4, ItemBuilder.asHead()
                .withHeadTexture(UsefulHead.EARTH.getTexture())
                .withName(ChatColor.DARK_AQUA + Language.getMessage(owner, "item.games_selector.name"))
                .build());

        this.addGameItem(21, LobbyGame.BEDWARS);
        this.addGameItem(22, LobbyGame.LASER_GAME);
        this.addGameItem(23, LobbyGame.RUSH_THE_FLAG);
        this.addGameItem(30, LobbyGame.BRIDGER);
        this.addGameItem(31, LobbyGame.THE_RUNNER);
        this.addGameItem(32, LobbyGame.PEARL_CONTROL);
    }

    private void addGameItem(int slot, LobbyGame game) {
        final IHyriGameInfo gameInfo = game.getGame();
        final List<String> lore = new ArrayList<>();
        final HyriNetworkCount playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCount();

        if (playerCount != null && playerCount.getCategory(gameInfo.getName()) != null) {
            lore.add(this.getMessage("lobby_selector", "connected") + ChatColor.AQUA + playerCount.getCategory(gameInfo.getName()).getPlayers());
        } else {
            lore.add(this.getMessage("lobby_selector", "connected") + ChatColor.AQUA + "0");
        }

        this.setItem(slot, new ItemBuilder(game.getIcon())
                .withName(ChatColor.DARK_AQUA + gameInfo.getDisplayName())
                .withLore(lore)
                .build(), event -> new GameTypeChooser(this.plugin, this.owner, game).open());
    }

}
