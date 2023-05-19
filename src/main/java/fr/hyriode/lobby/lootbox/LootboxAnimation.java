package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.packet.PacketUtil;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import fr.hyriode.lobby.util.CustomEntityItem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Objects;

/**
 * Created by AstFaster
 * on 21/04/2023 at 16:46
 */
public class LootboxAnimation {

    private static final int LEVITATION_TIME = 2;

    private final Player player;

    private AnimationArmorStand animationArmorStand;
    private ItemArmorStand itemArmorStand;

    private final HyriLobby plugin;
    private final LootboxReward rewards;
    private final LootboxReward.Item item;

    public LootboxAnimation(Player player, HyriLobby plugin, LootboxReward rewards) {
        this.player = player;
        this.plugin = plugin;
        this.rewards = rewards;
        this.item = this.rewards.getRandomItem();
    }

    public void start() {
        this.animationArmorStand = new AnimationArmorStand();
        this.animationArmorStand.createArmorStand(ItemBuilder.asHead(UsefulHead.COSMETICS_CHEST).build());
        this.animationArmorStand.sendArmorStand();
        this.animationArmorStand.triggerLevitation();
    }

    private void triggerExplosion() {
        final Location location = this.animationArmorStand.getLocation();

        new ParticleBuilder(ParticleEffect.EXPLOSION_NORMAL, location.clone().add(0, 1.80D, 0))
                .setOffset(0, 0, 0)
                .setSpeed(0.5F)
                .setAmount(100)
                .display(this.player);

        this.player.playSound(location, Sound.EXPLODE, 2.0f, 1.0f);
        this.animationArmorStand.destroyArmorStand();

        this.showItem();
    }

    private void showItem() {
        final IHyriPlayer account = IHyriPlayer.get(player.getUniqueId());
        final LootboxGiveContext context = new LootboxGiveContext(account, this.item.getIcon(), this.item.getName(this.player), true);

        this.item.give(context);

        account.update();

        if (context.isMessage()) {
            this.player.sendMessage(LobbyMessage.LOOTBOX_REWARD_MESSAGE.asString(this.player)
                    .replace("%reward%", Objects.requireNonNull(this.item.getName(this.player))));
        }

        this.itemArmorStand = new ItemArmorStand();
        this.itemArmorStand.show(context, this.animationArmorStand.getLocation());
    }

    public enum Shaking {
        RIGHT, LEFT
    }

    private class AnimationArmorStand {

        private EntityArmorStand armorStand;
        private Location location;

        public void triggerLevitation() {
            new BukkitRunnable() {

                private int index = 0;

                private double y = 0.0d;
                private float pitch = 0.0f;

                private Shaking shaking = Shaking.RIGHT;

                @Override
                public void run() {
                    if (this.index % 5 == 0) {
                        if (this.shaking == Shaking.RIGHT) {
                            this.pitch = 5.0F;
                            this.shaking = Shaking.LEFT;
                        } else if (this.shaking == Shaking.LEFT) {
                            this.pitch = 355.0f;
                            this.shaking = Shaking.RIGHT;
                        }
                    }

                    armorStand.setHeadPose(new Vector3f(0, 0, index == LEVITATION_TIME * 20 ? 0.0f : this.pitch));

                    sendArmorStandMetadata();

                    if (index == LEVITATION_TIME * 20) {
                        Bukkit.getScheduler().runTaskLater(plugin, LootboxAnimation.this::triggerExplosion, 30L);
                        this.cancel();
                        return;
                    }

                    if (index % 2 * 20 == 0) {
                        player.playSound(location, Sound.ITEM_PICKUP, 2.0F, 1.0F + ((float) this.index / LEVITATION_TIME * 20));
                    }

                    this.y += 0.08D;

                    updateLocation(this.y);
                    teleportArmorStand();

                    new ParticleBuilder(ParticleEffect.CLOUD, location.clone().add(0, 1.70D, 0))
                            .setOffset(0, 0, 0)
                            .setSpeed(0.1F)
                            .setAmount(1)
                            .display(player);

                    this.index++;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }

        private void createArmorStand(ItemStack head) {
            final Location playerLocation = player.getLocation().clone();
            final Vector direction = playerLocation.getDirection();

            if (this.location == null) {
                this.location = playerLocation.toVector().add(direction.multiply(4.0D)).toLocation(player.getWorld());
                this.location.setDirection(playerLocation.subtract(this.location).toVector());
            }

            this.armorStand = new EntityArmorStand(((CraftWorld) this.location.getWorld()).getHandle());
            this.armorStand.setEquipment(EnumItemSlot.HELMET.getSlot(), CraftItemStack.asNMSCopy(head));
            this.armorStand.setInvisible(true);
            this.armorStand.setGravity(false);
            this.armorStand.setBasePlate(false);
            this.armorStand.setCustomNameVisible(false);

            this.updateLocation(0.0d);
        }

        private void destroyArmorStand() {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(this.armorStand.getId()));
        }

        private void teleportArmorStand() {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityTeleport(this.armorStand));
        }

        private void sendArmorStand() {
            if (this.armorStand != null) {
                PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntity(this.armorStand, 78));

                this.sendArmorStandMetadata();

                for (EnumItemSlot value : EnumItemSlot.values()) {
                    final int slot = value.getSlot();
                    final net.minecraft.server.v1_8_R3.ItemStack equipment = this.armorStand.getEquipment(slot);

                    if (equipment == null) {
                        continue;
                    }

                    PacketUtil.sendPacket(player, new PacketPlayOutEntityEquipment(this.armorStand.getId(), slot, equipment));
                }
            }
        }

        private void sendArmorStandMetadata() {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
        }

        private void updateLocation(double y) {
            final Location playerLocation = player.getLocation().clone();

            this.location.setY(playerLocation.getY() - 1.5D + y);

            if (this.armorStand != null) {
                this.armorStand.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
            }
        }

        public Location getLocation() {
            return this.location;
        }

    }

    private class ItemArmorStand {

        private EntityArmorStand armorStand;
        private CustomEntityItem entityItem;
        private Location location;

        private LootboxGiveContext context;

        public void show(LootboxGiveContext context, Location location) {
            this.context = context;
            this.location = location;

            this.createArmorStand();
            this.sendArmorStand();
            this.createItem();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                this.destroyItem();
                this.destroyArmorStand();

                player.playSound(this.location, Sound.ITEM_PICKUP, 2.0f, 1.0f);
            }, 5 * 20L);
        }

        private void createItem() {
            this.entityItem = CustomEntityItem.create(this.location, this.context.getFloatingItem());

            PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntity(this.entityItem, 2));
            PacketUtil.sendPacket(player, new PacketPlayOutEntityMetadata(this.entityItem.getId(), this.entityItem.getDataWatcher(), true));
            PacketUtil.sendPacket(player, new PacketPlayOutAttachEntity(0, this.entityItem, itemArmorStand.armorStand));
        }

        private void destroyItem() {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(this.entityItem.getId()));
        }

        private void createArmorStand() {
            this.armorStand = new EntityArmorStand(((CraftWorld) this.location.getWorld()).getHandle());
            this.armorStand.setInvisible(true);
            this.armorStand.setGravity(false);
            this.armorStand.setBasePlate(false);
            this.armorStand.setCustomNameVisible(true);
            this.armorStand.setCustomName(this.context.getFloatingText());
            this.armorStand.setLocation(this.location.getX(), this.location.getY() - 1.0D, this.location.getZ(), this.location.getYaw(), this.location.getPitch());
        }

        private void destroyArmorStand() {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(this.armorStand.getId()));
        }

        private void sendArmorStand() {
            if (this.armorStand != null) {
                PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntity(this.armorStand, 78));
                PacketUtil.sendPacket(player, new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));

                for (EnumItemSlot value : EnumItemSlot.values()) {
                    final int slot = value.getSlot();
                    final net.minecraft.server.v1_8_R3.ItemStack equipment = this.armorStand.getEquipment(slot);

                    if (equipment == null) {
                        continue;
                    }

                    PacketUtil.sendPacket(player, new PacketPlayOutEntityEquipment(this.armorStand.getId(), slot, equipment));
                }
            }
        }

        public Location getLocation() {
            return this.location;
        }

    }

}
