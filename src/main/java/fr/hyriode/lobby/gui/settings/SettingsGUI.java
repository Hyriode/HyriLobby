package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.api.settings.SettingsLevel;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.Language;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SettingsGUI extends LobbyGUI {

    private boolean clicked;

    private final IHyriPlayerSettings settings;
    private final boolean withLang;

    public SettingsGUI(HyriLobby plugin, Player owner, boolean withLang) {
        super(owner, plugin, () -> "settings", 54);
        this.withLang = withLang;

        this.settings = this.account.getSettings();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.applyDesign(Design.BORDER);

        final List<SettingsLevel> reducedLevels = Arrays.asList(SettingsLevel.ALL, SettingsLevel.FRIENDS, SettingsLevel.NONE);

        this.addItem(20, new BooleanItem(Material.NETHER_STAR, LobbyMessage.SETTINGS_AUTO_QUEUE_NAME, LobbyMessage.SETTINGS_AUTO_QUEUE_DESCRIPTION)
                .withValueProvider(this.settings::isAutoQueueEnabled)
                .withValueModifier(this.settings::setAutoQueueEnabled));

        this.addItem(21, new BooleanItem(Material.NAME_TAG, LobbyMessage.SETTINGS_FRIEND_CONNECTION_NAME, LobbyMessage.SETTINGS_FRIEND_CONNECTION_DESCRIPTION)
                .withValueProvider(this.settings::isFriendConnectionNotificationEnabled)
                .withValueModifier(this.settings::setFriendConnectionNotificationEnabled));

        this.addItem(22, new BooleanItem(Material.PAPER, LobbyMessage.SETTINGS_FRIEND_REQUEST_NAME, LobbyMessage.SETTINGS_FRIEND_REQUEST_DESCRIPTION)
                .withValueProvider(this.settings::isFriendRequestsEnabled)
                .withValueModifier(this.settings::setFriendRequestsEnabled));

        this.addItem(23, new LevelItem(Material.PAPER, LobbyMessage.SETTINGS_PARTY_REQUEST_NAME, LobbyMessage.SETTINGS_PARTY_REQUEST_DESCRIPTION)
                .withValueProvider(this.settings::getPartyRequestsLevel)
                .withValueModifier(this.settings::setPartyRequestsLevel)
                .withAvailableValues(reducedLevels));

        this.addItem(24, new LevelItem(Material.BOOK, LobbyMessage.SETTINGS_GLOBAL_CHAT_NAME, LobbyMessage.SETTINGS_GLOBAL_CHAT_DESCRIPTION)
                .withValueProvider(this.settings::getGlobalChatLevel)
                .withValueModifier(this.settings::setGlobalChatLevel)
                .withAvailableValues(reducedLevels));

        this.addItem(30, new LevelItem(Material.BOOK_AND_QUILL, LobbyMessage.SETTINGS_PRIVATE_MSG_NAME, LobbyMessage.SETTINGS_PRIVATE_MSG_DESCRIPTION)
                .withValueProvider(this.settings::getPrivateMessagesLevel)
                .withValueModifier(this.settings::setPrivateMessagesLevel)
                .withAvailableValues(reducedLevels));

        this.addItem(31, new LevelItem(HEAD_ITEM.apply(UsefulHead.NOTE_BLOCK).build(), LobbyMessage.SETTINGS_PRIVATE_MSG_SOUND_NAME, LobbyMessage.SETTINGS_PRIVATE_MSG_SOUND_DESCRIPTION)
                .withValueProvider(this.settings::getPrivateMessagesSoundLevel)
                .withValueModifier(this.settings::setPrivateMessagesSoundLevel)
                .withAvailableValues(reducedLevels));

        this.addItem(32, new LevelItem(Material.EYE_OF_ENDER, LobbyMessage.SETTINGS_VISIBILITY_NAME, LobbyMessage.SETTINGS_VISIBILITY_DESCRIPTION)
                .withValueProvider(this.settings::getPlayersVisibilityLevel)
                .withValueModifier(value -> {
                    this.settings.setPlayersVisibilityLevel(value);
                    this.updateVisibility();
                })
                .withAvailableValues(Arrays.asList(SettingsLevel.values())));

        if (this.withLang) {
            this.setItem(49, Language.getFrom(this.settings.getLanguage()).createItem(this.account), event -> this.openWithGoBack(49, new LanguageGUI(this.plugin, this.owner)));
        }
    }

    private <T> void addItem(int slot, Item<T> item) {
        final List<T> availableValues = item.getAvailableValues();
        final T currentValue = item.getValueProvider().get();

        for (int i = 0; i < availableValues.size(); i++) {
            if (availableValues.get(i).equals(currentValue)) {
                item.setCurrentValueIndex(i);
                break;
            }
        }

        final ItemStack itemStack = new ItemBuilder(item.getItemStack())
                .withName(item.getName(this.account))
                .withLore(this.getItemLore(item))
                .build();

        this.setItem(slot, itemStack, event -> {
            if (!event.getClick().isLeftClick()) {
                return;
            }

            this.clicked = true;

            this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);

            int valueIndex = item.getCurrentValueIndex() + 1;

            if (valueIndex >= availableValues.size()) {
                valueIndex = 0;
            }

            item.setCurrentValueIndex(valueIndex);
            item.getValueModifier().accept(availableValues.get(valueIndex));

            final ItemStack currentItem = event.getCurrentItem();
            final ItemMeta meta = currentItem.getItemMeta();

            meta.setLore(this.getItemLore(item));
            currentItem.setItemMeta(meta);
        });
    }

    private <T> List<String> getItemLore(Item<T> item) {
        final List<String> lore = item.getDescription(this.account);
        final T currentValue = item.getValueProvider().get();

        lore.add("");
        lore.add(LobbyMessage.SETTINGS_STATUS.asString(this.account));

        for (T availableValue : item.getAvailableValues()) {
            lore.add(ChatColor.DARK_GRAY + Symbols.DOT_BOLD + " " + (currentValue.equals(availableValue) ? ChatColor.AQUA : ChatColor.GRAY) + item.getValueFormatter().apply(availableValue).asString(this.account));
        }

        lore.add("");
        lore.add(LobbyMessage.CLICK_TO_CHANGE.asString(this.account));

        return lore;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);

        this.account.update();

        if (this.clicked) {
            this.owner.sendMessage(LobbyMessage.SETTINGS_UPDATED_MESSAGE.asString(this.account));
        }

        this.updateVisibility();
    }

    private void updateVisibility() {
        this.plugin.getPlayerManager().getLobbyPlayer(this.owner.getUniqueId()).initPlayersVisibility(this.settings.getPlayersVisibilityLevel(), false);
    }

    private static class Item<T> {

        private int currentValueIndex;

        protected final ItemStack itemStack;
        protected final LobbyMessage name;
        protected final LobbyMessage description;

        protected Supplier<T> valueProvider = () -> null;
        protected Consumer<T> valueModifier = value -> {};
        protected Function<T, LobbyMessage> valueFormatter;

        protected List<T> availableValues;

        public Item(ItemStack itemStack, LobbyMessage name, LobbyMessage description) {
            this.itemStack = itemStack;
            this.name = name;
            this.description = description;
        }

        public Item(Material material, LobbyMessage name, LobbyMessage description) {
            this(new ItemStack(material), name, description);
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public String getName(IHyriPlayer account) {
            return this.name.asString(account);
        }

        public List<String> getDescription(IHyriPlayer account) {
            return this.description.asList(account);
        }

        public Supplier<T> getValueProvider() {
            return this.valueProvider;
        }

        public Item<T> withValueProvider(Supplier<T> valueProvider) {
            this.valueProvider = valueProvider;
            return this;
        }

        public Consumer<T> getValueModifier() {
            return this.valueModifier;
        }

        public Item<T> withValueModifier(Consumer<T> valueModifier) {
            this.valueModifier = valueModifier;
            return this;
        }

        public Function<T, LobbyMessage> getValueFormatter() {
            return this.valueFormatter;
        }

        public Item<T> withValueFormatter(Function<T, LobbyMessage> valueFormatter) {
            this.valueFormatter = valueFormatter;
            return this;
        }

        public List<T> getAvailableValues() {
            return this.availableValues;
        }

        public Item<T> withAvailableValues(List<T> availableValues) {
            this.availableValues = availableValues;
            return this;
        }

        private int getCurrentValueIndex() {
            return this.currentValueIndex;
        }

        private void setCurrentValueIndex(int currentValueIndex) {
            this.currentValueIndex = currentValueIndex;
        }

    }

    private static class BooleanItem extends Item<Boolean> {

        public BooleanItem(ItemStack itemStack, LobbyMessage name, LobbyMessage description) {
            super(itemStack, name, description);
            this.withValueFormatter(value -> value ? LobbyMessage.SETTINGS_ON : LobbyMessage.SETTINGS_OFF);
            this.withAvailableValues(Arrays.asList(true, false));
        }

        public BooleanItem(Material material, LobbyMessage name, LobbyMessage description) {
            this(new ItemStack(material), name, description);
        }

    }

    private static class LevelItem extends Item<SettingsLevel> {

        public LevelItem(ItemStack itemStack, LobbyMessage name, LobbyMessage description) {
            super(itemStack, name, description);
            this.withValueFormatter(value -> {
                switch (value) {
                    case PARTY:
                        return LobbyMessage.SETTINGS_LEVEL_PARTY;
                    case FRIENDS:
                        return LobbyMessage.SETTINGS_LEVEL_FRIENDS;
                    case NONE:
                        return LobbyMessage.SETTINGS_LEVEL_NONE;
                    default:
                        return LobbyMessage.SETTINGS_LEVEL_ALL;
                }
            });
        }

        public LevelItem(Material material, LobbyMessage name, LobbyMessage description) {
            this(new ItemStack(material), name, description);
        }

    }

}
