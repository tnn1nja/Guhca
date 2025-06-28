package net.tnn1nja.guhca.behavior;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashSet;

import static net.tnn1nja.guhca.Guhca.guhca;

public class RaidBellExtender extends BehaviorCore {

    public static int vanillaBellDetectionRadius = 32;
    public static int customBellDetectionRadius = 128;
    public static HashSet<Location> bellOnCooldownLocations = new HashSet<Location>();

    @EventHandler
    public void onBellRing(BellRingEvent e){
        Location l = e.getBlock().getLocation();
        if(!isRaidersWithinVanillaRange(l)){
            Collection<Raider> raiders = getRaidersWithinCustomRange(l);
            if(!raiders.isEmpty() && !bellOnCooldownLocations.contains(l)) {
                Bukkit.getScheduler().runTaskLater(guhca, new Runnable() {
                    @Override
                    public void run() {
                        for (LivingEntity le: raiders) {
                            le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                        }
                    }
                }, 60L);
                Bukkit.getScheduler().runTaskLater(guhca, new Runnable() {
                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(l, Sound.BLOCK_BELL_RESONATE, 1, 1);
                        }
                    }
                }, 10L);
                startBellCooldown(l);
                guhca.log("Bell resonated with range of " + customBellDetectionRadius + " blocks");
            }
        }
    }

    @EventHandler
    public void onBellResonate(BellResonateEvent e){
        Location l = e.getBlock().getLocation();
        e.getResonatedEntities().addAll(getRaidersWithinCustomRange(l));
        startBellCooldown(l);
        guhca.log("Bell resonation range extended to " + customBellDetectionRadius + " blocks");
    }

    public static boolean isRaidersWithinVanillaRange(Location bellLocation){
        Collection<Entity> entities = bellLocation.getWorld().getNearbyEntities(bellLocation,
                vanillaBellDetectionRadius, vanillaBellDetectionRadius, vanillaBellDetectionRadius);
        for(Entity e: entities){
            if(e instanceof Raider && isWithinSphere(e.getLocation(), bellLocation, vanillaBellDetectionRadius)){
                return true;
            }
        }
        return false;
    }

    public static void startBellCooldown(Location l){
        bellOnCooldownLocations.add(l);
        Bukkit.getScheduler().runTaskLater(guhca, new Runnable() {
            @Override
            public void run() {
                bellOnCooldownLocations.remove(l);
            }
        }, 60L);
    }

    public static Collection<Raider> getRaidersWithinCustomRange(Location bellLocation){
        return bellLocation.getWorld().getNearbyEntities(bellLocation,
                        customBellDetectionRadius, customBellDetectionRadius, customBellDetectionRadius,
                        entity -> entity instanceof Raider &&
                                isWithinSphere(entity.getLocation(), bellLocation, customBellDetectionRadius)).
                stream().map(entity -> (Raider) entity).toList();
    }

    public static boolean isWithinSphere(Location entityLocation, Location bellLocation, int radius){
        return Math.pow((bellLocation.getX() - entityLocation.getX()), 2) +
                Math.pow((bellLocation.getY() - entityLocation.getY()), 2) +
                Math.pow((bellLocation.getZ() - entityLocation.getZ()), 2)
                <= Math.pow(radius, 2);
    }

}
