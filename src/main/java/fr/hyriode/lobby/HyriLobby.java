package fr.hyriode.lobby;

import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.jump.JumpHandler;
import fr.hyriode.lobby.leaderboard.LeaderboardHandler;
import fr.hyriode.lobby.listener.PlayerHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriLobby extends JavaPlugin {

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build();

    private IHyrame hyrame;

    private JumpHandler jumpHandler;
    private PlayerHandler playerHandler;
    private LeaderboardHandler leaderboardHandler;

    @Override
    public void onEnable() {
        this.getLogger().info("#====={------------------------------}=====#");
        this.getLogger().info("#====={   HyriLobby is starting...   }=====#");
        this.getLogger().info("#====={------------------------------}=====#");

        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));

        this.leaderboardHandler = new LeaderboardHandler(this);
        this.jumpHandler = this.hyrame.getListenerManager().getListener(JumpHandler.class);
        this.playerHandler = this.hyrame.getListenerManager().getListener(PlayerHandler.class);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("#====={------------------------------}=====#");
        this.getLogger().info("#====={  HyriLobby is now disabled ! }=====#");
        this.getLogger().info("#====={------------------------------}=====#");

        this.hyrame.getListenerManager().getListener(JumpHandler.class).stop();
    }

    public IHyrame getHyrame() {
        return this.hyrame;
    }

    public JumpHandler getJumpHandler() {
        return this.jumpHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }

    public LeaderboardHandler getLeaderboardHandler() {
        return this.leaderboardHandler;
    }
}
