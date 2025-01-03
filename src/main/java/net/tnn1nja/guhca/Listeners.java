package net.tnn1nja.guhca;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.UUID;

import static net.tnn1nja.guhca.Tools.*;
import static net.tnn1nja.guhca.Main.*;


public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        //Set Player Data
        p.setDisplayName(ChatColor.RED + p.getDisplayName() + ChatColor.RESET);
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
        if (Bukkit.getServerTickManager().isFrozen()) {
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

    @EventHandler
    public void onBellRing(BellRingEvent e){
        Location l = e.getBlock().getLocation();
        if(!isRaidersWithinVanillaRange(l)){
            Collection<Raider> raiders = getRaidersWithinCustomRange(l);
            if(!raiders.isEmpty() && !bellOnCooldownLocations.contains(l)) {
                Bukkit.getScheduler().runTaskLater(me, new Runnable() {
                    @Override
                    public void run() {
                        for (LivingEntity le: raiders) {
                            le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                        }
                    }
                }, 60L);
                Bukkit.getScheduler().runTaskLater(me, new Runnable() {
                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(l, Sound.BLOCK_BELL_RESONATE, 1, 1);
                        }
                    }
                }, 10L);
                startBellCooldown(l);
                log.info("[BetterRaidBells] Bell resonated with range of " + customDetectionRadius + " blocks.");
            }
        }
    }

    @EventHandler
    public void onBellResonate(BellResonateEvent e){
        Location l = e.getBlock().getLocation();
        e.getResonatedEntities().addAll(getRaidersWithinCustomRange(l));
        startBellCooldown(l);
        log.info("[BetterRaidBells] Bell resonation range extended to " + customDetectionRadius + " blocks.");
    }

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
    public void onToolDurabilityDecrease(PlayerItemDamageEvent e){
        Player p = e.getPlayer();
        ItemStack is = e.getItem();
        int maxDurability = is.getType().getMaxDurability();
        int remainingDurability = maxDurability - ((Damageable) is.getItemMeta()).getDamage();
        float durability = remainingDurability / (float) maxDurability;
        if (durability < 0.1){
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacy(ChatColor.RED + "Severe Warning: low durability"));
        }else if(durability < 0.2){
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacy(ChatColor.GOLD + "Warning: low durability"));
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame && e.getPlayer().isSneaking()){
            ItemFrame itf = (ItemFrame) e.getRightClicked();
            if(itf.getItem().getType() != Material.AIR) {
                itf.setVisible(!itf.isVisible());
                itf.setRotation(itf.getRotation().rotateCounterClockwise());
                log.info("[Guhca] Toggled Item Frame Visibility.");
            }
        }
    }

    @EventHandler
    public void onHitItemFrame(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof ItemFrame){
            ItemFrame itf = (ItemFrame) e.getEntity();
            if(!itf.isVisible()) {
                itf.setVisible(true);
                log.info("[Guhca] Toggled Item Frame Visibility.");
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
            delayedServerFreeze();
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

            if(e.getFinalDamage() > (p.getHealth() + p.getAbsorptionAmount()) &&
                    p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING &&
                    p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING &&
                    useRubyHeart(p)){
                p.setStatistic(Statistic.DAMAGE_TAKEN,
                        p.getStatistic(Statistic.DAMAGE_TAKEN) + ((int) e.getFinalDamage())*10);
                e.setDamage(0);
                p.setHealth(1);

                renderRubyParticles(p);
                p.teleport(getRespawnLocation(p));
                p.setFlying(false);
                renderRubyParticles(p);
            }
        }
    }

    @EventHandler
    public void onEndermanBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().getType().equals(EntityType.ENDERMAN)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceCustomItem(BlockPlaceEvent e){
        Material m = e.getBlock().getType();
        if (e.getItemInHand().getItemMeta().hasItemName() &&
                (m.equals(Material.COMMAND_BLOCK) || m.equals(Material.STRUCTURE_BLOCK))){
            e.setCancelled(true);
        }
    }

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