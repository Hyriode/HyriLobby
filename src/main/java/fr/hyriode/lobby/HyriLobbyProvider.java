package fr.hyriode.lobby;

import fr.hyriode.hyrame.plugin.IPluginProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriLobbyProvider implements IPluginProvider {

    public static final String PACKAGE = "fr.hyriode.lobby";

    private final HyriLobby plugin;

    public HyriLobbyProvider(HyriLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getId() {
        return "lobby";
    }

    @Override
    public String[] getCommandsPackages() {
        return new String[]{PACKAGE};
    }

    @Override
    public String[] getListenersPackages() {
        return new String[]{PACKAGE};
    }

    @Override
    public String[] getItemsPackages() {
        return new String[]{PACKAGE};
    }

    @Override
    public String getLanguagesPath() {
        return "/lang/";
    }
}
