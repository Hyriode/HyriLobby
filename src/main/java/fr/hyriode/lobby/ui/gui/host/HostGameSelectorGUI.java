package fr.hyriode.lobby.ui.gui.host;

import fr.hyriode.api.host.HostType;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AstFaster
 * on 30/07/2022 at 16:10
 */
public class HostGameSelectorGUI extends LobbyGUI {

    private static final int[] GAMES_SLOTS = new int[] {
            20, 21, 22, 23, 24,
            29, 30, 31, 32, 33
    };

   private HostType hostType = HostType.PUBLIC;

    public HostGameSelectorGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "host.game-selector", 6 * 9);

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setItem(4, ItemBuilder.asHead(UsefulHead.CHEST)
                .withName(LobbyMessage.SELECTOR_HOST_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.SELECTOR_HOST_ITEM_DESCRIPTION.asList(this.account))
                .build());

        this.setItem(49, new ItemBuilder(Material.ARROW)
                        .withName(LobbyMessage.BACK_ITEM.asString(this.account))
                        .build(),
                event -> new HostGUI(this.owner, this.plugin, true).open());

        this.addTypeItem();
        this.addGamesItem();
    }

    private void addTypeItem() {
        final List<String> lore = ListReplacer.replace(LobbyMessage.HOST_TYPE_ITEM_LORE.asList(this.account), "%public_color%", String.valueOf(this.hostType == HostType.PUBLIC ? ChatColor.AQUA : ChatColor.GRAY))
                .replace("%private_color%", String.valueOf(this.hostType == HostType.PRIVATE ? ChatColor.AQUA : ChatColor.GRAY))
                .list();

        this.setItem(47, new ItemBuilder(Material.EYE_OF_ENDER)
                .withName(LobbyMessage.HOST_TYPE_ITEM_NAME.asString(this.account))
                .withLore(lore)
                .build(),
                event -> {
                    this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);

                    final HostType[] types = HostType.values();
                    final int currentIndex = Arrays.asList(types).indexOf(this.hostType);

                    this.hostType = types.length <= currentIndex + 1 ? types[0] : types[currentIndex + 1];

                    this.addTypeItem();
                });
    }

    private void addGamesItem() {
        int slotIndex = 0;
        for (LobbyGame game : this.plugin.getGameManager().getGames()) {
            if (!game.isHostCompatible()) {
                continue;
            }

            if (GAMES_SLOTS.length <= slotIndex) {
                return;
            }

            final List<String> lore = new ArrayList<>();

            lore.add(game.formatType(this.owner));
            lore.add("");
            lore.addAll(game.getDescription(this.owner));
            lore.add("");
            lore.add(LobbyMessage.CLICK_TO_SELECT.asString(this.account));

            final ItemStack itemStack = new ItemBuilder(game.getIcon())
                    .withAllItemFlags()
                    .withName(ChatColor.AQUA + game.getGameInfo().getDisplayName())
                    .withLore(lore)
                    .build();

            this.setItem(GAMES_SLOTS[slotIndex], itemStack, event -> new HostGameTypeSelectorGUI(this.owner, this.plugin, game, this.hostType).open());

            slotIndex++;
        }
    }

}
