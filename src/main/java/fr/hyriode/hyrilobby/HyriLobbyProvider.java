package fr.hyriode.hyrilobby;

import fr.hyriode.hyrame.plugin.IPluginProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:17
 */
public class HyriLobbyProvider implements IPluginProvider {

    private static final String PACKAGE = "fr.hyriode.hyrilobby";

    private final JavaPlugin plugin;

    public HyriLobbyProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getId() {
        return "hyrilobby";
    }

    @Override
    public String[] getCommandsPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String[] getListenersPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String[] getItemsPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String getLanguagesPath() {
        return "/lang/";
    }
}
