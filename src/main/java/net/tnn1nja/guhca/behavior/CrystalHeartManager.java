package net.tnn1nja.guhca.behavior;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import static net.tnn1nja.guhca.Guhca.guhca;

public class CrystalHeartManager extends BehaviorCore {

    public static HashSet<UUID> damageImmunePlayers = new HashSet<UUID>();

    public static boolean useCrystalHeart(Player p){
        PlayerInventory inv = p.getInventory();
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack item = inv.getItem(i);
            if (item != null &&
                    item.getType() == Material.STRUCTURE_BLOCK &&
                    item.getItemMeta().hasItemName()){
                inv.setItem(i, null);
                return true;
            }
        }
        return false;
    }

    public static Location getRespawnLocation(Player p){
        if(p.getRespawnLocation() != null){
            return p.getRespawnLocation();
        }else{
            Random r = new Random();
            for(int i = 0; i<256; i++) {
                Location l = Bukkit.getWorlds().get(0).getSpawnLocation();
                l.setX(l.getX() + r.nextInt(19)-9);
                l.setZ(l.getZ() + r.nextInt(19)-9);
                for(int y = 319; y >= -64; y--){
                    l.setY(y);
                    Block b = l.getBlock();
                    if(!b.getType().isAir()) {
                        if (isFullBlock(b)) {
                            return l.add(0.5, 1, 0.5);
                        }else{
                            break;
                        }
                    }
                }
            }
            return Bukkit.getWorlds().get(0).getSpawnLocation().add(0.5, 0, 0.5);
        }
    }

    public static void doWorldCrystalRelocateAnim(Player p){
        double width;
        double height;
        double yMod;

        //Standing
        if(p.getPose() == Pose.STANDING){
            width = 0.35;
            height = 0.55;
            yMod = 1;
            //Sneaking
        }else if(p.getPose() == Pose.SNEAKING){
            width = 0.35;
            height = 0.45;
            yMod = 0.7;
            //Crawling
        }else if(p.getPose() == Pose.SWIMMING && !p.getLocation().getBlock().getType().equals(Material.WATER) ||
                (p.getPose() == Pose.SLEEPING)){
            width = 0.55;
            height = 0.35;
            yMod = 0.25;
            //Flying or Swimming
        }else{
            width = 0.55;
            height = 0.55;
            yMod = 0.25;
        }

        Location l = p.getLocation();
        World w = Bukkit.getWorlds().get(0);
        w.playSound(l, Sound.ENTITY_ITEM_BREAK, 1F, 1F);
        w.playSound(l, Sound.BLOCK_GLASS_BREAK, 1F, 1F);
        w.playSound(l, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 1F);
        w.spawnParticle(Particle.DUST, l.add(0, yMod,
                        0), 2048, width, height, width, 1,
                new Particle.DustOptions(Color.fromRGB(252, 47, 72), 1.2F), true);
    }

    public static void doClientCrystalObscureAnim(Player p){
        p.spawnParticle(Particle.DUST, p.getLocation().add(0, p.getEyeHeight(),
                        0), 512, 0.2, 0.2, 0.2, 1,
                new Particle.DustOptions(Color.fromRGB(252, 47, 72), 1.2F), true);
    }

    public static void grantPlayerImmunity(UUID uuid, long ticks){
        damageImmunePlayers.add(uuid);
        Bukkit.getScheduler().runTaskLater(guhca, new Runnable() {
            @Override
            public void run() {
                damageImmunePlayers.remove(uuid);
            }
        }, ticks);
    }

    public static boolean isFullBlock(Block b){
        BoundingBox bb = b.getBoundingBox();
        return (b.getCollisionShape().getBoundingBoxes().size() == 1 &&
                bb.getHeight() == 1.0 &&
                bb.getWidthX() == 1.0 &&
                bb.getWidthZ() == 1.0);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if(damageImmunePlayers.contains(p.getUniqueId())){
                e.setCancelled(true);
                return;
            }

            //Attempt to use Crystal Heart
            if(e.getFinalDamage() >= p.getHealth() &&
                    p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING &&
                    p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING &&
                    useCrystalHeart(p)){

                //Assorted
                p.setHealth(1.1);
                e.setDamage(0.1);
                p.setFreezeTicks(0);
                p.setFireTicks(0);
                p.setFallDistance(0);
                p.setGliding(false);
                grantPlayerImmunity(p.getUniqueId(), 60L);
                p.setStatistic(Statistic.DAMAGE_TAKEN,
                        p.getStatistic(Statistic.DAMAGE_TAKEN) + (((int) e.getFinalDamage())*10)-1);

                //Move with Animation
                doWorldCrystalRelocateAnim(p);
                Location respawn = getRespawnLocation(p);
                p.teleport(respawn);
                doClientCrystalObscureAnim(p);
                Bukkit.getScheduler().runTaskLater(guhca, new Runnable() {
                    @Override
                    public void run() {
                        p.setVelocity(new Vector(0, 0, 0));
                        p.teleport(respawn); //re-teleport to account for velocity
                        doWorldCrystalRelocateAnim(p);
                        doClientCrystalObscureAnim(p);
                        p.sendActionBar(Component.text("Your crystal heart has shattered"));
                    }
                }, 1L);

                //Enchanted Golden Apple
                for (PotionEffect pe: p.getActivePotionEffects()){
                    p.removePotionEffect(pe.getType());
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 6000, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));

                //Logging
                guhca.log("Crystal heart used");
            }
        }
    }

    @EventHandler
    public void onPlaceCrystalHeart(BlockPlaceEvent e){
        Material m = e.getBlock().getType();
        if (e.getItemInHand().getItemMeta().hasItemName() &&
                (m.equals(Material.STRUCTURE_BLOCK))){
            e.setCancelled(true);
        }
    }
}
