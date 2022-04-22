package fr.hyriode.lobby.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.rewards.hyris.HyrisReward;
import fr.hyriode.lobby.rewards.hyris.HyrisRewardManager;
import fr.hyriode.lobby.utils.LocationConverter;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class HyrisRewardCommand extends HyriCommand<HyriLobby> {

    private final Supplier<HyrisRewardManager> manager;

    public HyrisRewardCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("hyrisreward")
                .withDescription("Place a head where you are looking for, which give few Hyris when clicked")
                .withType(HyriCommandType.PLAYER)
                .withUsage("/hyrisreward <amount of reward>")
                .withPermission(player -> player.getRank().isStaff()));

        this.manager = () -> plugin.getRewardManager().getHyrisRewardManager();
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        this.handleArgument(ctx, "%integer%", output -> {
            final Player player = (Player) ctx.getSender();
            final int amount = output.get(0, Integer.class);
            final Block block = player.getTargetBlock((Set<Material>) null, 10);

            if (block.getType() != Material.SKULL) {
                player.sendMessage("§cYou must be looking at a skull to place a reward");
                return;
            }

            block.setMetadata(HyrisReward.METADATA_KEY, new FixedMetadataValue(this.plugin, UUID.randomUUID()));

            final Skull skull = (Skull) block.getState();
            skull.setSkullType(SkullType.PLAYER);

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", UsefulHead.MONEY.getTexture()));
            try {
                Field profileField = skull.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skull, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                player.sendMessage("§cAn error occurred while trying to set the head.");
                e.printStackTrace();
                return;
            }

            skull.update();
            this.manager.get().save(new HyrisReward(LocationConverter.toLobbyLocation(block.getLocation()), amount), block.getMetadata(HyrisReward.METADATA_KEY).get(0).asString());
        });
    }
}
