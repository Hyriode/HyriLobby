package fr.hyriode.hyrilobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.network.HyriNetworkCount;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.game.LobbyGame;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.InventoryUtil;
import fr.hyriode.hyrilobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GamesChooserGui extends LobbyInventory {

    public GamesChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "games_selector", "game-selector", 54);

        this.init();
    }

    protected void init() {
        this.inventory.clear();

        this.fill();

        this.addGameItem(21, LobbyGame.LASER_GAME);
        this.addGameItem(22, LobbyGame.BEDWARS);
        this.addGameItem(23, LobbyGame.THE_RUNNER);
        this.addGameItem(30, LobbyGame.BRIDGER);
        this.addGameItem(31, LobbyGame.PEARL_CONTROL);
        this.addGameItem(32, LobbyGame.RUSH_THE_FLAG);

        this.addOtherItems();
    }

    private void addOtherItems()  {
        ItemStack hostItem = new ItemBuilder(Material.COMMAND_MINECART)
                .withName(LobbyMessage.HOST_ITEM.getGuiItem(this.guiName).getForPlayer(this.owner))
                .withLore(LobbyMessage.SOON_LINE.get().getForPlayer(this.owner))
                .build();

        ItemStack vipItem = new ItemBuilder(Material.GOLD_INGOT)
                .withName(LobbyMessage.VIP_ITEM.getGuiItem(this.guiName).getForPlayer(this.owner))
                .build();

        ItemStack jumpItem = new ItemBuilder(Material.FEATHER)
                .withName(LobbyMessage.JUMP_ITEM.getGuiItem(this.guiName).getForPlayer(this.owner))
                .build();

        this.setItem(4, hostItem);
        this.setItem(48, vipItem, event -> this.owner.teleport(this.plugin.getConfiguration().getVipLocation().asBukkit()));
        this.setItem(50, jumpItem, event -> this.owner.teleport(this.plugin.getConfiguration().getJumpLocation().asBukkit()));
    }

    private void addGameItem(int slot, LobbyGame game) {
        final IHyriGameInfo gameInfo = game.getGame();
        final List<String> lore = new ArrayList<>();
        final HyriNetworkCount playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCount();

        if (playerCount != null && playerCount.getCategory(gameInfo.getName()) != null) {
            lore.add(game.getGameTypeLine(this.owner));
            lore.add("");
            lore.addAll(game.getDescription(this.owner));
            lore.add("");
            lore.add(LobbyMessage.LOBBY_PLAYERS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + "0");
            lore.add("");
            lore.add(LobbyMessage.CONNECT_LINE.get().getForPlayer(this.owner));
        } else {
            lore.add(game.getGameTypeLine(this.owner));
            lore.add("");
            lore.addAll(game.getDescription(this.owner));
            lore.add("");
            lore.add(LobbyMessage.LOBBY_PLAYERS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + "0");
            lore.add("");
            lore.add(LobbyMessage.CONNECT_LINE.get().getForPlayer(this.owner));
        }

        this.setItem(slot, new ItemBuilder(game.getIcon())
                .withName(ChatColor.BLUE + gameInfo.getDisplayName())
                .withLore(lore)
                .build(), event -> new GameTypeChooser(this.plugin, this.owner, game).open());
    }

}
