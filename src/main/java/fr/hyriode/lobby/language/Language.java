package fr.hyriode.lobby.language;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemHead;
import fr.hyriode.lobby.util.ListUtil;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 29/06/2022 at 13:13
 */
public enum Language implements ItemHead {

    FRENCH(HyriLanguage.FR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ=="),
    ENGLISH(HyriLanguage.EN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5ZDk5ZDljNDY0NzRlMjcxM2E3ZTg0YTk1ZTRjZTdlOGZmOGVhNGQxNjQ0MTNhNTkyZTQ0MzVkMmM2ZjlkYyJ9fX0=")
    ;

    private HyriLanguageMessage display;
    private final Supplier<HyriLanguageMessage> displaySupplier;

    private final HyriLanguage initial;
    private final String texture;

    Language(HyriLanguage initial, String texture) {
        this.displaySupplier = () -> HyriLanguageMessage.get("lang." + initial.getCode() + ".display");
        this.initial = initial;
        this.texture = texture;
    }

    public HyriLanguage getInitial() {
        return this.initial;
    }

    public HyriLanguageMessage getDisplay() {
        return this.display != null ? this.display : (this.display = this.displaySupplier.get());
    }

    public String getDisplay(IHyriPlayer account) {
        return this.getDisplay().getValue(account);
    }

    @Override
    public String getTexture() {
        return this.texture;
    }

    public ItemStack createItem(IHyriPlayer account) {
        return ItemBuilder.asHead()
                .withName(LobbyMessage.LANG_SETTINGS_ITEM_NAME.asString(account))
                .withLore(ListUtil.replace(LobbyMessage.LANG_SETTINGS_ITEM_LORE.asList(account), "%lang%", this.getDisplay(account)))
                .withHeadTexture(this.texture)
                .build();
    }

    public static Language getFrom(HyriLanguage language) {
        for (Language head : values()) {
            if (head.getInitial() == language) {
                return head;
            }
        }
        return Language.FRENCH;
    }

}
