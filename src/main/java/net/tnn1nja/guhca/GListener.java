package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static net.tnn1nja.guhca.GTools.OFPGen;
import static net.tnn1nja.guhca.Main.*;
import static net.tnn1nja.guhca.GTools.PlayersDied;


public class GListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        //set Visibility
        p.setDisplayName(ChatColor.RED + p.getDisplayName() + ChatColor.WHITE);
        p.setPlayerListName(ChatColor.WHITE + p.getName());
        Online.addEntry(p.getName());

        //Set Values
        afkTracker.put(p.getUniqueId(), 0);
        e.setJoinMessage(ChatColor.YELLOW + p.getName() + " joined the game.");

        //Checks
        if(playersDied){ p.setGameMode(GameMode.SPECTATOR); }

        //OFPGen
        if(!p.hasPlayedBefore()){
            OFPGen();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        afkTracker.replace(p.getUniqueId(), 0);
        if(Afk.getEntries().contains(p.getName())) {
            Online.addEntry(p.getName());
            p.setPlayerListName(ChatColor.WHITE + p.getName());
        }
    }

    //Fix Regen Soup Disappearing
    @EventHandler
    public void onSoup(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        ItemStack i = e.getItem();

        if(i.getType().equals(Material.SUSPICIOUS_STEW)){
            if(i.hasItemMeta()){
                SuspiciousStewMeta stew = (SuspiciousStewMeta) i.getItemMeta();
                if(stew.hasCustomEffects() && stew.hasCustomEffect(PotionEffectType.REGENERATION)){
                    log.info("[Gucha] " + p.getName() + "'s Regen Soup Fixed.");
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        afkTracker.remove(p.getUniqueId());
        e.setQuitMessage(ChatColor.GOLD + p.getName() + " left the game.");

        if(kicked){
            e.setQuitMessage(ChatColor.RED + p.getName() + " was kicked.");
            kicked = false;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        e.setDeathMessage(ChatColor.RED + e.getDeathMessage());
        PlayersDied();
    }

    //@HonouraryEventHandler
    public static void onSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable(){
            public void run() {
                for(Player p: Bukkit.getOnlinePlayers()){
                    UUID uuid = p.getUniqueId();
                    afkTracker.replace(uuid, afkTracker.get(uuid)+1);

                    if(afkTracker.get(uuid) > afkTime && Online.getEntries().contains(p.getName())){
                        Afk.addEntry(p.getName());
                        p.setPlayerListName(ChatColor.GRAY + "" + ChatColor.ITALIC  + p.getName());
                    }
                }

            }
        }, 0L, 20L);
    }
}