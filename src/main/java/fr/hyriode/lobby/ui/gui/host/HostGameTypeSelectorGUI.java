package fr.hyriode.lobby.ui.gui.host;

import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.host.HostRequest;
import fr.hyriode.api.host.HostType;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.ui.gui.selector.game.GameTypeSelectorGUI;
import fr.hyriode.lobby.game.host.HostWaitingAnimation;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 30/07/2022 at 16:10
 */
public class HostGameTypeSelectorGUI extends LobbyGUI {

    private final LobbyGame game;
    private final HostType hostType;

    public HostGameTypeSelectorGUI(Player owner, HyriLobby plugin, LobbyGame game, HostType hostType) {
        super(owner, plugin, () -> "host.game-type-selector", 6 * 9);
        this.game = game;
        this.hostType = hostType;

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
                event -> new HostGameSelectorGUI(this.owner, this.plugin).open());

        this.addTypesItem();
    }

    private void addTypesItem() {
        final List<IHyriGameType> types = this.game.getGameInfo().getTypes().stream().sorted(Comparator.comparingInt(IHyriGameType::getId)).collect(Collectors.toList());

        for (IHyriGameType type : types) {
            for (int i : GameTypeSelectorGUI.SlotConfiguration.getSlots(types.size())) {
                if (this.inventory.getItem(i) != null) {
                    continue;
                }

                this.setItem(i, new ItemBuilder(this.game.getIcon())
                        .withName(ChatColor.AQUA + type.getDisplayName())
                        .withLore("", LobbyMessage.HOST_CLICK_TO_CREATE.asString(this.account))
                        .build(),
                        event -> {
                            new HostRequest(this.hostType, this.owner.getUniqueId(), this.game.getName(), type.getName()).send();
                            new HostWaitingAnimation(this.owner).start(this.plugin);

                            this.plugin.getHostHandler().addWaitingPlayer(this.owner.getUniqueId());

                            this.owner.closeInventory();
                        });
                break;
            }
        }
    }

}
