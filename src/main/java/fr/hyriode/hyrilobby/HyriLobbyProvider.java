package fr.hyriode.hyrilobby;

import fr.hyriode.hyrame.plugin.IPluginProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriLobbyProvider implements IPluginProvider {

    private final JavaPlugin plugin;

    public HyriLobbyProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String[] getCommandsPackages() {
        //TODO Add aux Références les packages
        return new String[0];
    }

    @Override
    public String[] getListenersPackages() {
        //TODO Add aux Références les packages
        return new String[0];
    }

    @Override
    public String getLanguagesPath() {
        return "/lang/";
    }
}
