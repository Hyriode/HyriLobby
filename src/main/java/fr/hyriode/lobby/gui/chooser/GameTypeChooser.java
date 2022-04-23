package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.HyriGameType;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.games.LobbyGame;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameTypeChooser extends LobbyInventory {

    private static final List<Integer> DONT_FILL = Arrays.asList(19, 19 + 9, 25, 25 + 9);

    private final LobbyGame game;
    private final ItemBuilder builder;

    public GameTypeChooser(HyriLobby plugin, Player owner, LobbyGame game) {
        super(owner, plugin, "game_type_selector", game.getGame().getTypes().size() > 5 ? 54 : 45);

        this.game = game;
        this.builder = new ItemBuilder(game.getIcon());

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(0, new ItemBuilder(Material.COMPASS).withName(this.getMessage("back")).build(), e -> new GamesChooserGui(this.plugin, this.owner).open());
        this.setItem(4, this.builder.clone()
                .withName(ChatColor.DARK_AQUA + this.game.getGame().getDisplayName())
                .withLore(this.getMessage("lobby_selector", "connected") + this.getPlayersCount())
                .build());

        int i = 20;
        for (Map.Entry<String, HyriGameType> entry : this.game.getGame().getTypes().entrySet()) {
            this.setItem(i, this.builder.clone()
                    .withName(ChatColor.DARK_AQUA + entry.getValue().getDisplayName())
                    .withLore(this.getMessage("lobby_selector", "connected") + this.getPlayersCount(), "", ChatColor.DARK_AQUA + HyriLanguageMessage.get("message.click-to-play").getForPlayer(this.owner))
                    .build(), event -> {
                HyriAPI.get().getQueueManager().addPlayerInQueueWithPartyCheck(this.owner.getUniqueId(), this.game.getGame().getName(), entry.getKey());

                this.owner.sendMessage(ChatColor.AQUA + "Vous avez été ajouté dans la queue pour " + this.game.getGame().getDisplayName() + " " + entry.getValue().getDisplayName());
            });

            if (i == 24) {
                i = 29;
            } else {
                i++;
            }
        }
    }

    private int getPlayersCount() {
        return HyriAPI.get().getNetworkManager().getNetwork().getPlayerCount().getCategory(this.game.getGame().getName()).getPlayers();
    }

}
