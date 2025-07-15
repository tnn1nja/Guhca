package net.tnn1nja.guhca;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static net.tnn1nja.guhca.Tools.*;
import static net.tnn1nja.guhca.Main.*;


public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(playersDied){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        playersDied();
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
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
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
                log.info("Crystal heart used");
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