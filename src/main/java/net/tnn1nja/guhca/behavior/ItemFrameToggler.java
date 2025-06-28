package net.tnn1nja.guhca.behavior;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static net.tnn1nja.guhca.Guhca.guhca;

public class ItemFrameToggler extends BehaviorCore {

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame itf && e.getPlayer().isSneaking()){
            if(itf.getItem().getType() != Material.AIR) {
                itf.setVisible(!itf.isVisible());
                itf.getWorld().playSound(itf.getLocation(), Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1, 1);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHitItemFrame(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof ItemFrame itf){
            if(!itf.isVisible()) {
                itf.setVisible(true);
                guhca.log("Toggled item frame visibility");
            }
        }
    }

}
