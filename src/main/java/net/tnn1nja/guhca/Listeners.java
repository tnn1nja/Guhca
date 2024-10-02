package net.tnn1nja.guhca;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static net.tnn1nja.guhca.Tools.*;
import static net.tnn1nja.guhca.Main.*;


public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        //Set Player Data
        p.setDisplayName(ChatColor.RED + p.getDisplayName());
        p.setPlayerListName(ChatColor.WHITE + p.getName());
        Online.addEntry(p.getName());
        e.setJoinMessage(ChatColor.YELLOW + p.getName() + " joined the game.");
        afkTracker.put(p.getUniqueId(), 0);

        //Check if Players Died
        if(playersDied){
            p.setGameMode(GameMode.SPECTATOR);
        }

        //Setup New Player
        if(!p.hasPlayedBefore()){
            generateOfflinePlayerSet();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give " + p.getName() + " *");
        }

        //Unfreeze Server
        if (Bukkit.getOnlinePlayers().size() == 1) { //includes joining player
            serverResume();
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
                    log.info("[Guhca] " + p.getName() + "'s Regen Soup Fixed.");
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        afkTracker.remove(p.getUniqueId());

        //Quit Message
        if(kicker == null) {
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " left the game.");
        }else if(kicker.equals(".afk")){
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " took damage while afk.");
        }else if(kicker.equals(".lag")){
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " lagged out.");
        }else {
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " was kicked by " + kicker + ".");
        }
        kicker = null;

        //Freeze Server
        if(Bukkit.getOnlinePlayers().size() == 1){ //includes leaving player
            serverFreeze();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        e.setDeathMessage(ChatColor.RED + stripMCCodes(e.getDeathMessage()));
        playersDied();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(Afk.hasPlayer(p)){
                p.kickPlayer("You took damage will afk\n");
                kicker = ".afk";
            }
        }
    }

    /*
    @EventHandler
    public void onGoatHorn(PlayerInteractEvent e){
        ItemStack i = e.getItem();
        if (i.getType() == Material.GOAT_HORN) {
            Bukkit.getPlayer("ItsAllGud").kickPlayer("Goat Horn was misused.");
        }
    }
     */

    //@HonouraryEventHandler
    public static void onSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable(){
            public void run() {
                //AFK Tracker
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

    //@HonouraryEventHandler
    public static void onHalfSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable(){
            public void run(){
                //Kick Lagging Players
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(p.getPing() > pingKickThreshold){
                        p.kickPlayer("Your ping exceeded " + pingKickThreshold);
                        kicker = ".lag";
                    }
                }
            }
        }, 0L, 10L);
    }
}