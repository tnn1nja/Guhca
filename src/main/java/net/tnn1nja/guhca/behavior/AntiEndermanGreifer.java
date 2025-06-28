package net.tnn1nja.guhca.behavior;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AntiEndermanGreifer extends BehaviorCore {

    @EventHandler
    public void onEndermanBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().getType().equals(EntityType.ENDERMAN)) {
            e.setCancelled(true);
        }
    }

}
