package fr.hyriode.lobby.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.network.counter.IHyriCategoryCounter;
import fr.hyriode.api.util.Skin;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StoreCategory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 23/06/2022 at 11:31
 */
public class LobbyGame {

    protected NPCData npcData;
    protected StoreCategory storeCategory;

    protected final String name;
    protected final ItemStack icon;
    protected final State state;
    protected final HyriLanguageMessage description;
    protected final HyriLanguageMessage type;

    protected final IHyriGameInfo gameInfo;

    protected boolean usedInSelector = true;

    public LobbyGame(String name, ItemStack icon, State state) {
        this.name = name;
        this.icon = icon;
        this.state = state;
        this.description = HyriLanguageMessage.get("game." + this.name + ".description");
        this.type = HyriLanguageMessage.get("game." + this.name + ".type");
        this.gameInfo = HyriAPI.get().getGameManager().getGameInfo(name);
    }

    public LobbyGame(String name, Material icon, State state) {
        this(name, new ItemStack(icon), state);
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getIcon() {
        return this.icon.clone();
    }

    public State getState() {
        return this.state;
    }

    public HyriLanguageMessage getDescription() {
        return this.description;
    }

    public List<String> getDescription(Player player) {
        final List<String> outputLore = new ArrayList<>();
        final String[] lines = this.description.getValue(player).split("\n");

        for(String line : lines){
            outputLore.add(ChatColor.GRAY + line);
        }
        return outputLore;
    }

    public HyriLanguageMessage getType() {
        return this.type;
    }

    public String formatType(Player player) {
        return LobbyMessage.TYPE_LINE.asString(player) + this.type.getValue(player);
    }

    public IHyriGameInfo getGameInfo() {
        return this.gameInfo;
    }

    public IHyriCategoryCounter getPlayerCounter() {
        return HyriAPI.get().getNetworkManager().getNetwork().getPlayerCounter().getCategory(this.name);
    }

    public int getPlayers() {
        final IHyriCategoryCounter counter = this.getPlayerCounter();

        if (counter == null) {
            return 0;
        }
        return counter.getPlayers();
    }

    public boolean isUsedInSelector() {
        return this.usedInSelector;
    }

    public void setUsedInSelector(boolean usedInSelector) {
        this.usedInSelector = usedInSelector;
    }

    public StoreCategory getStoreCategory() {
        return this.storeCategory;
    }

    public boolean hasStoreCategory() {
        return this.storeCategory != null;
    }

    public NPCData getNPCData() {
        return this.npcData;
    }

    public enum State {

        NEW("new"),
        POPULAR("popular"),
        OPENED("opened"),
        SOON("soon"),
        BLOCKED("blocked");

        private HyriLanguageMessage display;

        private final String key;

        State(String key) {
            this.key = "game.state." + key + ".display";
        }

        public HyriLanguageMessage getDisplay() {
            return this.display == null ? this.display = HyriLanguageMessage.get(this.key) : this.display;
        }

    }

    public static class NPCData {

        private final Location location;
        private final Skin skin;
        protected final Map<EnumItemSlot, ItemStack> equipment;

        public NPCData(Location location, Skin skin) {
            this.location = location;
            this.skin = skin;
            this.equipment = new HashMap<>();
        }

        public Location getLocation() {
            return this.location;
        }

        public Skin getSkin() {
            return this.skin;
        }

        public NPCData addEquipment(EnumItemSlot slot, Material material) {
            this.equipment.put(slot, new ItemStack(material));
            return this;
        }

        public NPCData addEquipment(EnumItemSlot slot, ItemStack itemStack) {
            this.equipment.put(slot, itemStack);
            return this;
        }

        public Map<EnumItemSlot, ItemStack> getEquipment() {
            return this.equipment;
        }

    }

}
