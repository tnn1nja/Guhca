package net.tnn1nja.guhca.behavior;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;

public class ExperienceDelayRemover extends BehaviorCore {

    @EventHandler
    public void onExperienceOrb(PlayerExpCooldownChangeEvent e){
        if(e.getReason() == PlayerExpCooldownChangeEvent.ChangeReason.PICKUP_ORB) {
            e.setNewCooldown(0);
        }
    }

}
