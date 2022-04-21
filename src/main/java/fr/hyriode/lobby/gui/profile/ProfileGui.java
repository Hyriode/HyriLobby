package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.DurationConverter;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGui;
import fr.hyriode.lobby.gui.settings.SettingsGui;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
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
        super(owner, plugin, "profile", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(21, HEAD_ITEM.apply(UsefulHead.CRATE).withName(this.getMessage("friends")).withLore(this.getMessage("friends.lore")).build(),
                e -> new FriendsGui(this.plugin, this.owner).open()
        );
        this.setItem(22, PLAYER_HEAD_ITEM.apply(this.owner.getUniqueId()).withName(this.getMessage("account")).withLore(this.getAccountLore()).build());
        this.setItem(23, new ItemBuilder(new Potion(PotionType.WATER)).withName(ChatColor.DARK_AQUA + "Boosters").withLore(this.getMessage("global", "not_implemented")).build());
        this.setItem(30, HEAD_ITEM.apply(UsefulHead.GOLD_BLOCK).withName(ChatColor.DARK_AQUA + "Hyri+").withLore(this.getHyriPlusLore()).build());
        this.setItem(31, new ItemBuilder(Material.REDSTONE_COMPARATOR).withName(this.getMessage("settings", "name")).withLore(this.getMessage("settings", "lore")).build(),
                e -> new SettingsGui(this.plugin, this.owner).open()
        );
        this.setItem(32, HEAD_ITEM.apply(UsefulHead.EARTH).withName(ChatColor.DARK_AQUA + ChatColor.stripColor(this.getMessage("settings", "language"))).withLore(this.getMessage("language.lore")).build(),
                e -> new LanguageGui(this.plugin, this.owner).open()
        );
    }

    private List<String> getAccountLore() {
        final List<String> lore = new ArrayList<>();

        lore.add(this.getMessage("rank") + this.account.getRank().getPrefix());
        //TODO Check if player has Hyri+
        lore.add(ChatColor.WHITE + "Hyri+: " + (this.account.getRank().getType() == EHyriRank.ADMINISTRATOR ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));
        //TODO Get player level
        lore.add(this.getMessage("level") + this.account.getCurrentServer());
        lore.add(ChatColor.WHITE + "Hyris: " + ChatColor.AQUA + this.account.getHyris().getAmount());
        lore.add(this.getMessage("first_join") + DATE_FORMAT.format(this.account.getFirstLoginDate()));

        final DurationConverter duration = new DurationConverter(Duration.ofMillis(this.account.getPlayTime()));
        final int minutes = duration.toMinutesPart();
        duration.substract(minutes * 60);
        lore.add(this.getMessage("playtime") + Duration.of(duration.toSeconds(), ChronoUnit.SECONDS).toHours() + "h " + minutes + "m");

        return lore;
    }

    private List<String> getHyriPlusLore() {
        final List<String> lore = new ArrayList<>();

        //TODO Check if player doesn't have Hyri+
        if (this.account.getRank().getType() != EHyriRank.ADMINISTRATOR) {
            lore.add(this.getMessage("hyriplus.dont_have"));
            return lore;
        }
        //TODO Hyri+ buy date
        lore.add(this.getMessage("hyriplus.buy_date") + DATE_FORMAT.format(this.account.getFirstLoginDate()));
        lore.add(this.getMessage("hyriplus.expire_date") + DATE_FORMAT.format(this.account.getFirstLoginDate()));
        lore.add(this.getMessage("hyriplus.remaining") + DATE_FORMAT.format(this.account.getFirstLoginDate()));

        return lore;
    }
}
