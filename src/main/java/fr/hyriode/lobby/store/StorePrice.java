package fr.hyriode.lobby.store;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.utils.Symbols;
import net.md_5.bungee.api.ChatColor;

import java.text.NumberFormat;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Created by AstFaster
 * on 09/04/2023 at 18:00
 */
public class StorePrice {

    private final Currency currency;
    private final long amount;

    public StorePrice(Currency currency, long amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public long getAmount() {
        return this.amount;
    }

    public enum Currency {

        HYRIS("hyris",
                (player, amount) -> player.getHyris().getAmount() >= amount,
                (player, amount) -> player.getHyris().remove(amount).exec(),
                amount -> ChatColor.LIGHT_PURPLE +  NumberFormat.getInstance().format(amount).replace(",", ".") + " ⛃"),

        HYODES("hyodes",
                (player, amount) -> player.getHyodes().getAmount() >= amount,
                (player, amount) -> player.getHyodes().remove(amount).exec(),
                amount -> ChatColor.GREEN + NumberFormat.getInstance().format(amount).replace(",", ".") + " " +  Symbols.SPARKLES);

        private HyriLanguageMessage display;

        private final String name;
        private final BiPredicate<IHyriPlayer, Long> amountCheck;
        private final BiConsumer<IHyriPlayer, Long> remove;
        private final Function<Long, String> formatter;

        Currency(String name, BiPredicate<IHyriPlayer, Long> amountCheck, BiConsumer<IHyriPlayer, Long> remove, Function<Long, String> formatter) {
            this.name = name;
            this.amountCheck = amountCheck;
            this.remove = remove;
            this.formatter = formatter;
        }

        public HyriLanguageMessage getDisplay() {
            return this.display == null ? this.display = HyriLanguageMessage.get("store.currency." + this.name + ".display") : this.display;
        }

        public boolean hasAmount(IHyriPlayer player, long amount) {
            return this.amountCheck.test(player, amount);
        }

        public void removeAmount(IHyriPlayer player, long amount) {
            this.remove.accept(player, amount);
        }

        public String formatAmount(long amount) {
            return this.formatter.apply(amount);
        }

    }

}
