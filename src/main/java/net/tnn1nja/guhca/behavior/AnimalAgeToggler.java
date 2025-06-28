package net.tnn1nja.guhca.behavior;

import org.bukkit.*;
import org.bukkit.entity.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.BoundingBox;

import static net.tnn1nja.guhca.Guhca.guhca;

public class AnimalAgeToggler extends BehaviorCore {

    @EventHandler @SuppressWarnings("deprecation")
    public void onRightClickEntity(PlayerInteractEntityEvent e){
        //Toggle Animal Aging
        if ((e.getRightClicked() instanceof Ageable a) && (a.getAge() < -1)) { //non-aging babies are always -1

            boolean toggleAgeLock = false;
            if (a.getAgeLock()) {
                //Test for Unlock
                PlayerInventory i = e.getPlayer().getInventory();
                if (i.getItemInMainHand().getType() == Material.MILK_BUCKET) {
                    i.setItemInMainHand(new ItemStack(Material.BUCKET));
                    toggleAgeLock = true;
                }
            } else {
                //Test for Lock
                PlayerInventory i = e.getPlayer().getInventory();
                if (i.getItemInMainHand().getType() == Material.SUGAR) {
                    i.getItemInMainHand().setAmount(i.getItemInMainHand().getAmount() - 1);
                    toggleAgeLock = true;
                }
            }

            //Toggle Locked Aging
            if(toggleAgeLock){
                World w = a.getWorld();
                BoundingBox bb = a.getBoundingBox();

                //Display Particles
                Particle p = a.getAgeLock() ? Particle.HAPPY_VILLAGER : Particle.WAX_ON;
                w.spawnParticle(p, bb.getCenter().toLocation(w).add(0, bb.getHeight()/5, 0),
                        15, bb.getWidthX()/2.5, bb.getHeight()/3.5, bb.getWidthZ()/2.5);

                //Play Sound
                Sound s = a.getAgeLock() ? Sound.ENTITY_GENERIC_DRINK : Sound.ENTITY_GENERIC_EAT;
                w.playSound(a, s, SoundCategory.NEUTRAL, 1F, 1.2F);

                //Cancel Event + Lock Aging + Logging
                e.getPlayer().swingMainHand();
                e.setCancelled(true);
                a.setAgeLock(!a.getAgeLock());
                String locked = a.getAgeLock() ? "locked at: " + a.getAge() : "unlocked";
                guhca.log(a.getType().toString() + " Aging " + locked);
            }

        }
    }

}
