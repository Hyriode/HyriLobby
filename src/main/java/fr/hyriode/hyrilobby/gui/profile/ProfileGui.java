package fr.hyriode.hyrilobby.gui.profile;

import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.DurationConverter;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.gui.settings.LanguageGui;
import fr.hyriode.hyrilobby.gui.settings.SettingsGui;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.RandomTools;
import fr.hyriode.hyrilobby.util.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ProfileGui extends LobbyInventory {

    public ProfileGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "profile", "profile", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        this.setItem(21, HEAD_ITEM.apply(UsefulHead.CRATE).withName(LobbyMessage.PROFILE_FRIENDS_NAME.getGuiItem(this.guiName).getForPlayer(this.owner)).withLore(LobbyMessage.PROFILE_FRIENDS_LORE.get().getForPlayer(this.owner)).build(),
                e -> new FriendsGui(this.plugin, this.owner).open()
        );
        this.setItem(22, PLAYER_HEAD_ITEM.apply(this.owner.getUniqueId()).withName(LobbyMessage.PROFILE_ACCOUNT.getGuiItem(this.guiName).getForPlayer(this.owner)).withLore(this.getAccountLore()).build());
        this.setItem(23, new ItemBuilder(new Potion(PotionType.WATER)).withName(ChatColor.DARK_AQUA + "Boosters").withLore(LobbyMessage.SOON_LINE.get().getForPlayer(this.owner)).build());
        this.setItem(30, HEAD_ITEM.apply(UsefulHead.GOLD_BLOCK).withName(ChatColor.DARK_AQUA + "Hyri+").withLore(this.getHyriPlusLore()).build());
        this.setItem(31, new ItemBuilder(Material.REDSTONE_COMPARATOR).withName(HyriLobby.getLanguageManager().getValue(this.owner, "gui.settings.name")).build(),
                e -> new SettingsGui(this.plugin, this.owner).open()
        );
        this.setItem(32, HEAD_ITEM.apply(UsefulHead.EARTH).withName(ChatColor.DARK_AQUA + LobbyMessage.SETTINGS_LANG.getGuiItem(this.guiName).getForPlayer(this.owner)).withLore(LobbyMessage.PROFILE_LANGUAGE_LORE.get().getForPlayer(this.owner)).build(),
                e -> new LanguageGui(this.plugin, this.owner).open()
        );
    }

    private List<String> getAccountLore() {
        final List<String> lore = new ArrayList<>();

        lore.add(LobbyMessage.PROFILE_RANK.getGuiItem(this.guiName).getForPlayer(this.owner) + this.account.getRank().getPrefix());
        lore.add(ChatColor.WHITE + "Hyri+: " + (this.account.hasHyriPlus() ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));
        lore.add(LobbyMessage.PROFILE_LEVEL.getGuiItem(this.guiName).getForPlayer(this.owner) + this.account.getNetworkLeveling().getLevel());
        lore.add(ChatColor.WHITE + "Hyris: " + ChatColor.AQUA + this.account.getHyris().getAmount());
        lore.add(LobbyMessage.PROFILE_FIRST_JOIN.getGuiItem(this.guiName).getForPlayer(this.owner) + DATE_FORMAT.format(this.account.getFirstLoginDate()));

        final DurationConverter duration = new DurationConverter(Duration.ofMillis(this.account.getPlayTime()));
        final int minutes = duration.toMinutesPart();
        duration.substract(minutes * 60);
        lore.add(LobbyMessage.PROFILE_PLAYTIME.getGuiItem(this.guiName).getForPlayer(this.owner) + Duration.of(duration.toSeconds(), ChronoUnit.SECONDS).toHours() + "h " + minutes + "m");

        return lore;
    }

    private List<String> getHyriPlusLore() {
        final List<String> lore = new ArrayList<>();

        if (!this.account.hasHyriPlus()) {
            lore.add(LobbyMessage.PROFILE_HYRIPLUS_DONT_HAVE.getGuiItem(this.guiName).getForPlayer(this.owner));
            return lore;
        }

        final HyriPlus hyriPlus = this.account.getHyriPlus();
        final DurationConverter converter = new DurationConverter(Duration.between(hyriPlus.getPurchaseDate().toInstant(), hyriPlus.getExpirationDate().toInstant()));

        lore.add(LobbyMessage.PROFILE_HYRIPLUS_BUY_DATE.getGuiItem(this.guiName).getForPlayer(this.owner) + DATE_FORMAT.format(hyriPlus.getPurchaseDate()));
        lore.add(LobbyMessage.PROFILE_HYRIPLUS_EXPIRE_DATE.getGuiItem(this.guiName).getForPlayer(this.owner) + DATE_FORMAT.format(hyriPlus.getExpirationDate()));
        lore.add(LobbyMessage.PROFILE_HYRIPLUS_REMAINING.getGuiItem(this.guiName).getForPlayer(this.owner) + RandomTools.getDurationMessage(converter, this.owner));

        return lore;
    }
}
