package fr.hyriode.lobby.listeners;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.TrapDoor;
import org.bukkit.metadata.FixedMetadataValue;

public class InteractHandler extends HyriListener<HyriLobby> {

    private static final String METADATA = "hyriode-interact";

    public InteractHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.PHYSICAL || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            return;
        }

        final Block block = event.getClickedBlock();

        if (block == null) {
            event.setCancelled(true);
            return;
        }

        final BlockState state = block.getState();
        //For doors, we need to update the top and bottom block
        //We can't retrieve the state multiple times, because it's creating a new state instance, so we can't update it
        final BlockState topState = block.getRelative(0, 1, 0).getType().getData() == Door.class ?
                block.getRelative(0, 1, 0).getState() : (block.getRelative(0, -1, 0).getType().getData() == Door.class ?
                block.getRelative(0, -1, 0).getState() : null);

        //Add Metadata to prevent spam clicking
        if (state.hasMetadata(METADATA)) {
            event.setCancelled(true);
            return;
        }

        if (topState != null && topState.hasMetadata(METADATA)) {
            event.setCancelled(true);
            return;
        }

        if (block.getType().getData() == TrapDoor.class) {
            final TrapDoor trapdoor = (TrapDoor) state.getData();
            this.invertState(state, null, trapdoor, null);
            return;
        }

        if (block.getType().getData() == Door.class) {
            if (topState != null) {
                final Door door = (Door) state.getData();
                this.invertState(state, topState, door, (Door) topState.getData());
            }
        }
    }

    private <T extends MaterialData & Openable> void invertState(BlockState state, BlockState topState, T block, T topBlock) {
        //Add Metadata to prevent spam clicking
        state.setMetadata(METADATA, new FixedMetadataValue(this.plugin, true));
        state.update(true);

        if (topState != null) {
            topState.setMetadata(METADATA, new FixedMetadataValue(this.plugin, true));
            topState.update(true);
        }


        //Two updates are required to update the block, idk why
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.update(state, topState, block, topBlock, false), 10L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.update(state, topState, block, topBlock, true), 20L);
    }

    private <T extends MaterialData & Openable> void update(BlockState state, BlockState topState, T block, T topBlock, boolean last) {
        if (block instanceof Door) {
            topBlock.setOpen(!topBlock.isOpen());
            topState.setData(topBlock);
            topState.update(true);
        }

        block.setOpen(!block.isOpen());
        state.setData(block);
        state.update(true);

        if (last) {
            state.removeMetadata(METADATA, this.plugin);
            state.update(true);

            if (topState != null) {
                topState.removeMetadata(METADATA, this.plugin);
                topState.update(true);
            }
        }
    }
}
