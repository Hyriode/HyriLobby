package fr.hyriode.lobby.rewards.hyris;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.utils.Language;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HyrisRewardHandler extends HyriListener<HyriLobby> {

    private final Supplier<HyrisRewardManager> manager;

    public HyrisRewardHandler(HyriLobby plugin) {
        super(plugin);

        this.manager = () -> plugin.getRewardManager().getHyrisRewardManager();
    }

    @EventHandler
    public void onHeadClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        final Block block = event.getClickedBlock();
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (block.getType() != Material.SKULL) {
            return;
        }

        if (!block.hasMetadata(HyrisReward.METADATA_KEY)) {
            return;
        }

        final HyrisReward reward = this.manager.get().get(block.getMetadata(HyrisReward.METADATA_KEY).get(0).asString());

        if (reward == null) {
            return;
        }

        if (reward.getFoundPlayers().contains(uuid)) {
            return;
        }

        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(uuid);

        reward.addFoundPlayer(uuid);
        account.getHyris().add(reward.getAmount());
        account.update();

        player.sendMessage(Language.getMessage(player, "rewards.message.base") + reward.getAmount() + ChatColor.AQUA + " Hyris");
    }
}
