package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.DurationFormatter;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.TimeUtil;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.gui.profile.booster.BoostersGUI;
import fr.hyriode.lobby.gui.settings.LanguageGUI;
import fr.hyriode.lobby.gui.settings.SettingsGUI;
import fr.hyriode.lobby.language.Language;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.text.NumberFormat;
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

        this.applyDesign(Design.BORDER);

        // Account information item
        this.setItem(13, ItemBuilder.asHead()
                .withName(LobbyMessage.PROFILE_ACCOUNT_NAME.asString(this.owner))
                .withLore(this.getAccountLore())
                .withPlayerHead(this.owner.getUniqueId())
                .build());

        // Friends
        this.setItem(22, HEAD_ITEM.apply(UsefulHead.CRATE)
                        .withName(LobbyMessage.PROFILE_FRIENDS_NAME.asString(this.account))
                        .withLore(LobbyMessage.PROFILE_FRIENDS_LORE.asList(this.account))
                        .build(),
                event -> this.openWithGoBack(49, new FriendsGUI(this.plugin, this.owner)));

        final ItemBuilder levelingItem = new ItemBuilder(Material.EXP_BOTTLE)
                .withName(LobbyMessage.PROFILE_LEVELING_NAME.asString(this.account))
                .withLore(this.getLevelingLore());

        // Leveling
        this.setItem(21, levelingItem.build(), event -> this.openWithGoBack(49, new LevelingRewardsGUI(this.owner, this.plugin, levelingItem.removeLoreLines(2).build())));

        // Hyri+
        this.setItem(30, HEAD_ITEM.apply(UsefulHead.GOLD_BLOCK)
                        .withName(ChatColor.AQUA + "Hyri+")
                        .withLore(this.getHyriPlusLore())
                        .build(),
                event -> {
                    if (this.account.getHyriPlus().has()) {
                        this.openWithGoBack(49, new PlusColorGUI(this.owner, this.plugin));
                    } else {
                        this.owner.chat("/store");
                    }
                });

        // Settings
        this.setItem(31, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                        .withName(LobbyMessage.PROFILE_SETTINGS_NAME.asString(this.account))
                        .withLore(LobbyMessage.PROFILE_SETTINGS_LORE.asList(this.account))
                        .build(),
                event -> this.openWithGoBack(49, new SettingsGUI(this.plugin, this.owner, false)));

        // Language
        this.setItem(32, Language.getFrom(this.account.getSettings().getLanguage()).createItem(this.account), event -> this.openWithGoBack(49, new LanguageGUI(this.plugin, this.owner)));

        // Boosters
        this.setItem(23, new ItemBuilder(new Potion(PotionType.WATER))
                        .withName(LobbyMessage.PROFILE_BOOSTERS_NAME.asString(this.account))
                        .withLore(LobbyMessage.PROFILE_BOOSTERS_LORE.asList(this.account))
                        .build(),
                event -> this.openWithGoBack(49, new BoostersGUI(this.plugin, this.owner)));

        // Lootboxes
        this.setItem(40, new ItemBuilder(Material.ENDER_CHEST)
                        .withName(LobbyMessage.PROFILE_LOOTBOXES_NAME.asString(this.account))
                        .withLore(LobbyMessage.PROFILE_LOOTBOXES_DESCRIPTION.asString(this.account))
                        .build(),
                event -> this.openWithGoBack(49, new LootboxesGUI(this.owner, this.plugin)));
    }

    private List<String> getAccountLore() {
        final List<String> lore = LobbyMessage.PROFILE_ACCOUNT_LORE.asList(this.account);
        final String playTime = new DurationFormatter().format(this.account.getSettings().getLanguage(), this.account.getStatistics().getTotalPlayTime());

        return ListReplacer.replace(lore, "%rank%", this.account.getRank().isDefault() ? HyriLanguageMessage.get("scoreboard.no-rank.value").getValue(this.account) : this.account.getPrefix())
                .replace("%premium%", this.account.getAuth().isPremium() ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD)
                .replace("%hyri+%", this.account.getHyriPlus().has() ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD)
                .replace("%hyris%", NumberFormat.getInstance().format(this.account.getHyris().getAmount()).replace(",", "."))
                .replace("%hyodes%", NumberFormat.getInstance().format(this.account.getHyodes().getAmount()).replace(",", "."))
                .replace("%level%", String.valueOf(this.account.getNetworkLeveling().getLevel()))
                .replace("%first_login%", TimeUtil.formatDate(new Date(this.account.getFirstLoginDate()), "dd/MM/yyyy HH:mm:ss"))
                .replace("%play_time%", playTime)
                .list();
    }

    private List<String> getLevelingLore() {
        final List<String> lore = LobbyMessage.PROFILE_LEVELING_LORE.asList(this.account);
        final BiConsumer<String, String> replacer = (character, value) -> ListReplacer.replace(lore, character, value);
        final IHyriLeveling leveling = this.account.getNetworkLeveling();
        final int currentLevel = leveling.getLevel();
        final double currentLevelExperience = leveling.getAlgorithm().levelToExperience(currentLevel);
        final double currentExperience = leveling.getExperience() - currentLevelExperience;
        final double totalExperience = leveling.getAlgorithm().levelToExperience(currentLevel + 1) - currentLevelExperience;
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

        final IHyriPlus hyriPlus = this.account.getHyriPlus();

        if (hyriPlus.has()) {
            final long enabledDate = hyriPlus.getEnabledDate();
            final Date purchase = new Date(enabledDate);
            final Date expiration = new Date(enabledDate + hyriPlus.getDuration() * 1000);

            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_BUY_DATE.asString(this.account), enabledDate <= 0 ? "-" : TimeUtil.formatDate(purchase));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_EXPIRE_DATE.asString(this.account), enabledDate <= 0 ? "-" : TimeUtil.formatDate(expiration));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_REMAINING.asString(this.account), enabledDate <= 0 ? "-" : new DurationFormatter().withDays(true).withSeconds(false).format(this.account.getSettings().getLanguage(), expiration.getTime() - purchase.getTime()));
            lineBuilder.accept(LobbyMessage.PROFILE_HYRIPLUS_PLUS_COLOR.asString(this.account), hyriPlus.getColor() + "+");

            lore.add("");
            lore.add(LobbyMessage.PROFILE_HYRIPLUS_CHANGE_COLOR.asString(this.account));
        } else {
            lore.add(LobbyMessage.PROFILE_HYRIPLUS_DONT_HAVE.asString(this.account));
            lore.add("");
            lore.add(LobbyMessage.PROFILE_HYRIPLUS_STORE_LINK.asString(this.account));
        }
        return lore;
    }

}
