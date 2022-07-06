package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.utils.DurationConverter;
import fr.hyriode.hyrame.utils.DurationFormatter;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.TimeUtil;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.gui.settings.LanguageGUI;
import fr.hyriode.lobby.gui.settings.SettingsGUI;
import fr.hyriode.lobby.language.Language;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.ListUtil;
import fr.hyriode.lobby.util.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

public class ProfileGUI extends LobbyGUI {

    public ProfileGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "profile", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.border();

        // Account information item
        this.setItem(13, ItemBuilder.asHead()
                .withName(LobbyMessage.PROFILE_ACCOUNT.asString(this.owner))
                .withLore(this.getAccountLore())
                .withPlayerHead(this.owner.getUniqueId())
                .build());

        // Friends
        this.setItem(22, HEAD_ITEM.apply(UsefulHead.CRATE)
                .withName(LobbyMessage.PROFILE_FRIENDS_NAME.asString(this.account))
                .withLore(LobbyMessage.PROFILE_FRIENDS_LORE.asList(this.account))
                .build(),
                event -> this.openWithGoBack(49, new FriendsGUI(this.plugin, this.owner)));

        // Leveling
        this.setItem(21, new ItemBuilder(Material.EXP_BOTTLE)
                .withName(LobbyMessage.PROFILE_LEVELING_NAME.asString(this.account))
                .withLore(this.getLevelingLore())
                .build());

        // Statistics viewer
        this.setItem(23, new ItemBuilder(Material.BOOK_AND_QUILL)
                .withName(LobbyMessage.PROFILE_STATS_NAME.asString(this.account))
                .withLore(LobbyMessage.PROFILE_STATS_LORE.asList(this.account))
                .build());

        // Hyri+
        this.setItem(30, HEAD_ITEM.apply(UsefulHead.GOLD_BLOCK)
                .withName(ChatColor.AQUA + "Hyri+")
                .withLore(this.getHyriPlusLore())
                .build(),
                event -> this.owner.chat("/store"));

        // Settings
        this.setItem(31, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                        .withName(LobbyMessage.PROFILE_SETTINGS_NAME.asString(this.account))
                        .withLore(LobbyMessage.PROFILE_SETTINGS_LORE.asList(this.account))
                        .build(),
                event -> this.openWithGoBack(49, new SettingsGUI(this.plugin, this.owner, false)));

        // Language
        this.setItem(32, Language.getFrom(this.account.getSettings().getLanguage()).createItem(this.account), event -> this.openWithGoBack(49, new LanguageGUI(this.plugin, this.owner)));

        // Boosters
        this.setItem(40, new ItemBuilder(new Potion(PotionType.WATER))
                .withName(LobbyMessage.PROFILE_BOOSTERS_NAME.asString(this.account))
                .withLore(LobbyMessage.PROFILE_BOOSTERS_LORE.asList(this.account))
                .build());
    }

    private List<String> getAccountLore() {
        final List<String> lore = new ArrayList<>();
        final BiConsumer<String, String> lineBuilder = (prefix, value) -> lore.add(ChatColor.DARK_GRAY + Symbols.DOT_BOLD + ChatColor.GRAY + " " + prefix + ": " + ChatColor.AQUA + value);
        final String playtime = new DurationFormatter().format(this.account.getSettings().getLanguage(), this.account.getPlayTime());

        lineBuilder.accept(LobbyMessage.PROFILE_RANK.asString(this.account), this.account.getPrefix());
        lineBuilder.accept("Hyri+", this.account.hasHyriPlus() ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘");
        lineBuilder.accept(LobbyMessage.PROFILE_LEVEL.asString(this.account), String.valueOf(this.account.getNetworkLeveling().getLevel()));
        lineBuilder.accept("Hyris", ChatColor.LIGHT_PURPLE + String.valueOf(this.account.getHyris().getAmount()));
        lineBuilder.accept(LobbyMessage.PROFILE_FIRST_JOIN.asString(this.account), TimeUtil.formatDate(this.account.getFirstLoginDate(), "dd/MM/yyyy HH:mm:ss"));
        lineBuilder.accept(LobbyMessage.PROFILE_PLAYTIME.asString(this.account), playtime);

        return lore;
    }

    private List<String> getLevelingLore() {
        final List<String> lore = LobbyMessage.PROFILE_LEVELING_LORE.asList(this.account);
        final BiConsumer<String, String> replacer = (character, value) -> ListUtil.replace(lore, character, value);
        final IHyriLeveling leveling = this.account.getNetworkLeveling();
        final int currentLevel = leveling.getLevel();
        final double currentExperience = leveling.getExperience();
        final double totalExperience = leveling.getAlgorithm().levelToExperience(currentLevel + 1);
        final int percentage = (int) Math.floor(currentExperience / totalExperience * 100);

        final StringBuilder progressBar = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            if (percentage >= i * 2) {
                progressBar.append(ChatColor.AQUA);
            } else {
                progressBar.append(ChatColor.GRAY);
            }
            progressBar.append(Symbols.LINE_VERTICAL_BOLD);
        }

        replacer.accept("%current_level%", String.valueOf(currentLevel));
        replacer.accept("%progress_bar%", progressBar.toString());
        replacer.accept("%percentage%", percentage + "%");
        replacer.accept("%experience%", String.valueOf(totalExperience - currentExperience));

        return lore;
    }

    private List<String> getHyriPlusLore() {
        final List<String> lore = LobbyMessage.PROFILE_HYRIPLUS_EXPLANATION.asList(this.account);
        final BiConsumer<String, String> lineBuilder = (prefix, value) -> lore.add(ChatColor.DARK_GRAY + Symbols.DOT_BOLD + ChatColor.GRAY + " " + prefix + ": " + ChatColor.AQUA + value);

        lore.add("");

        if (this.account.hasHyriPlus()) {
            final HyriPlus hyriPlus = this.account.getHyriPlus();
            final Date purchase = hyriPlus.getPurchaseDate();
            final Date expiration = hyriPlus.getExpirationDate();

            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_BUY_DATE.asString(this.account), TimeUtil.formatDate(purchase));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_EXPIRE_DATE.asString(this.account), TimeUtil.formatDate(expiration));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_REMAINING.asString(this.account), new DurationFormatter().withDays(true).withSeconds(false).format(this.account.getSettings().getLanguage(), expiration.getTime() - purchase.getTime()));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_PLUS_COLOR.asString(this.account), hyriPlus.getPlusColor() + "+");
        } else {
            lore.add(LobbyMessage.PROFILE_HYRIPLUS_DONT_HAVE.asString(this.account));
        }

        lore.add("");
        lore.add(LobbyMessage.PROFILE_HYRIPLUS_STORE_LINK.asString(this.account));

        return lore;
    }

}
