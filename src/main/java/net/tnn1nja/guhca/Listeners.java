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
        if(!playerDeaths.containsKey(p.getName())) {
            playerDeaths.put(p.getName(), 0);
        }

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
        }else if(kicker.equals("afk")){
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " took damage while afk.");
        }else if(kicker.equals("lag")){
            e.setQuitMessage(ChatColor.GOLD + p.getName() + " took damage while lagging.");
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
        //Fix Death Message
        e.setDeathMessage(ChatColor.RED + stripColor(e.getDeathMessage()));

        //Flag Players as Dead
        playersDied();

        //Add a Death to the Counter
        String pname = e.getEntity().getPlayer().getName();
        playerDeaths.replace(pname, playerDeaths.get(pname)+1);
        survived = Tools.getSecsSurvived();
        Tools.saveData();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            log.info(p.getName() + " was hit.");
            if(Afk.hasPlayer(p)){
                p.kickPlayer("You took damage will afk\n");
                kicker = "afk";
            }
            if(lagTracker.contains(p.getUniqueId())) {
                lagTracker.remove(p.getName());
                p.kickPlayer("You took damage whilst lagging.");
                kicker = "lag";
            }
        }
    }

    //@HonouraryEventHandler
    public static void onSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable(){
            public void run() {
                //Lag Tracker
                for(Player p: Bukkit.getOnlinePlayers()){
                    if (p.getPing() > lagPing){
                        lagTracker.add(p.getUniqueId());
                    }else if(lagTracker.contains(p.getUniqueId())){
                        lagTracker.remove(p.getUniqueId());
                    }
                }

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
}