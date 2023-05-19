package fr.hyriode.lobby.util;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 18/05/2023 at 18:31
 */
public class CustomEntityItem extends EntityItem {

    public static CustomEntityItem create(Location location, ItemStack itemStack) {
        final World world = ((CraftWorld) location.getWorld()).getHandle();
        final CustomEntityItem item = new CustomEntityItem(world);

        item.setItemStack(CraftItemStack.asNMSCopy(itemStack));
        item.setPosition(location.getX(), location.getY(), location.getZ());

        return item;
    }

    public CustomEntityItem(World world) {
        super(world);
        this.noclip = true;
    }

    @Override
    public void t_() {
        this.u();
        this.r();
        this.ticksLived = 0;
    }

    @Override
    public void K() {

    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {

    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {

    }

    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {

    }

    @Override
    public void f(NBTTagCompound nbttagcompound) {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean ad() {
        return false;
    }

    @Override
    public boolean ae() {
        return false;
    }

    @Override
    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void die() {

    }

    @Override
    public void G() {

    }

    @Override
    public void a(int i) {
        super.a(Integer.MAX_VALUE);
    }

    @Override
    protected void burn(float i) {

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void d(EntityHuman entityhuman) {

    }

    @Override
    public void c(int i) {

    }

}
