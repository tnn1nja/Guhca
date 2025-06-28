package net.tnn1nja.guhca.behavior;

import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class CampfireBooster extends BehaviorCore {

    public static HashMap<UUID, Boolean> campfireBoostSoundTracker = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        campfireBoostSoundTracker.put(e.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        //Campfire Boosting
        if(p.isGliding()) {
            boolean aboveLitCampfire = false;
            boolean isHayBaled = false;
            int distance = 1;
            Block testBlock = p.getLocation().getBlock();
            while (true) {
                if (testBlock.getType().equals(Material.AIR) || !testBlock.isSolid()) {
                    testBlock = testBlock.getRelative(BlockFace.DOWN);
                    distance++;
                } else if (testBlock.getType().equals(Material.CAMPFIRE)) {
                    Campfire cf = (Campfire) testBlock.getBlockData();
                    isHayBaled = cf.isSignalFire();
                    if (isHayBaled || distance < 9) {
                        aboveLitCampfire = cf.isLit();
                    }
                    break;
                } else {
                    break;
                }

                if (distance > 21) {
                    break;
                }
            }

            if (aboveLitCampfire) {
                double lambda = 0.3;
                double maxVelocity = isHayBaled ? 1.5 : 1;
                Vector v = p.getVelocity();
                if(v.getY() < maxVelocity) {
                    v.setY(v.getY() + (lambda * (maxVelocity - v.getY())));
                    p.setVelocity(v);
                }
                if(!campfireBoostSoundTracker.get(p.getUniqueId())){
                    p.getWorld().playSound(p.getLocation(), "guhca.campfire_boost", SoundCategory.PLAYERS,
                            1, 1);
                    campfireBoostSoundTracker.replace(p.getUniqueId(), true);
                }
            } else if(campfireBoostSoundTracker.get(p.getUniqueId())){
                campfireBoostSoundTracker.replace(p.getUniqueId(), false);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        campfireBoostSoundTracker.remove(e.getPlayer().getUniqueId());
    }

}
