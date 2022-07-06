package fr.hyriode.lobby.store;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AstFaster
 * on 02/07/2022 at 16:57
 */
public class StoreCategory extends StoreIcon {

    private HyriLanguageMessage name;
    private HyriLanguageMessage description;

    protected final List<StoreIcon> contents;

    public StoreCategory(ItemStack itemStack, String id) {
        super(id, itemStack);
        this.contents = new ArrayList<>();
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return null;
    }

    public String getId() {
        return this.id;
    }

    public void addContent(StoreIcon... icons) {
        this.contents.addAll(Arrays.asList(icons));
    }

    public List<StoreIcon> getContents() {
        return this.contents;
    }

    public StoreIcon getContent(String id) {
        for (StoreIcon content : this.contents) {
            if (content.getId().equals(id)) {
                return content;
            }
        }
        return null;
    }

    public StoreItem getItem(String id) {
        for (StoreIcon content : this.contents) {
            if (content.getId().equals(id) && content instanceof StoreItem) {
                return (StoreItem) content;
            }
        }
        return null;
    }

    @Override
    public HyriLanguageMessage getName() {
        return this.name == null ? this.name = HyriLanguageMessage.get("store.category." + this.id + ".name") : this.name;
    }

    @Override
    public HyriLanguageMessage getDescription() {
        return this.description == null ? this.description = HyriLanguageMessage.get("store.category." + this.id + ".description") : this.description;
    }

}
