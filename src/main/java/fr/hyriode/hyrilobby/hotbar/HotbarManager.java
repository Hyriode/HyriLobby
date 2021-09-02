package fr.hyriode.hyrilobby.hotbar;

import fr.hyriode.common.item.ItemBuilder;
import fr.hyriode.hyrilobby.gui.SettingsGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HotbarManager {

    private Player p;

    public HotbarManager(Player p) {

        this.p = p;
    }

    public void addItemsOnJoin() {

        Inventory i = p.getInventory();
        i.clear();

        ItemStack pHead = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withSkullOwner(p.getUniqueId())
                .withName("§fVos Informations")
                .withLore("§fFaites Clic-Droit pour accéder à vos Informations")
                .build();
        ItemStack compass = new ItemBuilder(Material.COMPASS)
                .withName("§fSélection des Mini-Jeux")
                .withLore("§fFaites Clic-Droit pour Choisir votre Mini-Jeu")
                .build();
        ItemStack emerald = new ItemBuilder(Material.EMERALD)
                .withName("§fOuvrir le Shop")
                .withLore("§fFaites Clic-Droit pour Ouvrir le Shop")
                .build();
        ItemStack comparator = new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .withName("§fOuvrir les Paramètres")
                .withLore("§fFaites Clic-Droit pour Personnaliser votre Jeu")
                .withEvent(PlayerInteractEvent.class, itemSupplier -> {
                    Player p = ((PlayerInteractEvent) itemSupplier.get()).getPlayer();
                    new SettingsGui(p).open();
                })
                .build();
        ItemStack netherStar = new ItemBuilder(Material.NETHER_STAR)
                .withName("§fChanger de Lobby")
                .withLore("§fFaites Clic-Droit pour Choisir votre Lobby")
                .build();

        i.setItem(0, compass);
        i.setItem(1, pHead);
        i.setItem(4, emerald);
        i.setItem(7, comparator);
        i.setItem(8, netherStar);
    }
}
