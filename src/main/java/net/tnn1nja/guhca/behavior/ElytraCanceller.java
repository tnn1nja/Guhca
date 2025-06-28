package net.tnn1nja.guhca.behavior;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ElytraCanceller extends BehaviorCore {

    @EventHandler
    public void onElytraSneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        if (p.isGliding() && e.isSneaking()){
            p.setGliding(false);
        }
    }

}
