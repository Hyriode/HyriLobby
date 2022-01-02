package fr.hyriode.lobby.util;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 26/08/2021 at 13:59
 */
public class References {

    public static final String SERVER_NAME = "Hyriode";
    public static final String SERVER_IP = "hyriode.fr";
    public static final String WEBSITE_URL = "www.hyriode.fr";
    public static final String STORE_WEBSITE_URL = "store.hyriode.fr";
    public static final String DISCORD_URL = "discord.hyriode.fr";

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build();
}
