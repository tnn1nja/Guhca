package net.tnn1nja.guhca;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.event.block.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

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
        afkTracker.put(p.getUniqueId(), (Integer) 0);
        campfireBoostSoundTracker.put(e.getPlayer().getUniqueId(), false);

        //Check if Players Died
        if(playersDied){
            p.setGameMode(GameMode.SPECTATOR);
        }

        //Setup New Player
        if(!p.hasPlayedBefore()){
            generateOfflinePlayerSet();
        }

        //Unfreeze Server
        if (Bukkit.getServerTickManager().isFrozen()) {
            serverResume();
        }

        //Discover all Recipes
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof Keyed){
                p.discoverRecipe(((Keyed) recipe).getKey());
            }
        });

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        //AFK Tracker
        afkTracker.replace(p.getUniqueId(), 0);
        if(Afk.getEntries().contains(p.getName())) {
            Online.addEntry(p.getName());
            p.setPlayerListName(ChatColor.WHITE + p.getName());
        }

        //Campfire Boosting
        if(p.isGliding()) {
            boolean aboveLitCampfire = false;
            boolean isHayBaled = false;
            boolean passedThroughBlock = false;
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
                    if(passedThroughBlock){
                        break;
                    }else {
                        passedThroughBlock = true;
                    }
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
                    p.playSound(p.getLocation(), "guhca.campfire_boost", SoundCategory.PLAYERS, 1, 1);
                    campfireBoostSoundTracker.replace(p.getUniqueId(), true);
                }
            } else if(campfireBoostSoundTracker.get(p.getUniqueId())){
                campfireBoostSoundTracker.replace(p.getUniqueId(), false);
            }
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
                log.info("[Guhca] Bell resonated with range of " + customBellDetectionRadius + " blocks.");
            }
        }
    }

    @EventHandler
    public void onBellResonate(BellResonateEvent e){
        Location l = e.getBlock().getLocation();
        e.getResonatedEntities().addAll(getRaidersWithinCustomRange(l));
        startBellCooldown(l);
        log.info("[Guhca] Bell resonation range extended to " + customBellDetectionRadius + " blocks.");
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
    public void onToggleAging(PlayerInteractEntityEvent e){
        //Toggle Animal Aging
        if ((e.getRightClicked() instanceof Ageable a) && (a.getAge() < -1)) { //non-aging babies are always -1

            boolean toggleAgeLock = false;
            if (a.getAgeLock()) {
                //Test for Unlock
                PlayerInventory i = e.getPlayer().getInventory();
                if (i.getItemInMainHand().getType() == Material.MILK_BUCKET) {
                    i.setItemInMainHand(new ItemStack(Material.BUCKET));
                    toggleAgeLock = true;
                }
            } else {
                //Test for Lock
                PlayerInventory i = e.getPlayer().getInventory();
                if (i.getItemInMainHand().getType() == Material.SUGAR) {
                    i.getItemInMainHand().setAmount(i.getItemInMainHand().getAmount() - 1);
                    toggleAgeLock = true;
                }
            }

            //Toggle Locked Aging
            if(toggleAgeLock){
                World w = a.getWorld();
                BoundingBox bb = a.getBoundingBox();

                //Display Particles
                Particle p = a.getAgeLock() ? Particle.HAPPY_VILLAGER : Particle.WAX_ON;
                w.spawnParticle(p, bb.getCenter().toLocation(w).add(0, bb.getHeight()/5, 0),
                        15, bb.getWidthX()/2.5, bb.getHeight()/3.5, bb.getWidthZ()/2.5);

                //Play Sound
                Sound s = a.getAgeLock() ? Sound.ENTITY_GENERIC_DRINK : Sound.ENTITY_GENERIC_EAT;
                w.playSound(a, s, SoundCategory.NEUTRAL, 1F, 1.2F);

                //Cancel Event + Lock Aging + Logging
                e.getPlayer().swingMainHand();
                e.setCancelled(true);
                a.setAgeLock(!a.getAgeLock());
                String locked = a.getAgeLock() ? "Locked at: " + a.getAge() + "." : "Unlocked.";
                log.info("[Guhca] " + a.getType().toString() + " Aging " + locked);
            }

        }

        //Toggle Item Frame Visibility
        if (e.getRightClicked() instanceof ItemFrame && e.getPlayer().isSneaking()){
            ItemFrame itf = (ItemFrame) e.getRightClicked();
            if(itf.getItem().getType() != Material.AIR) {
                itf.setVisible(!itf.isVisible());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractAtEntityEvent e){
        //Toggle Armor Stand Pose
        if(e.getRightClicked() instanceof ArmorStand as){
            if(e.getPlayer().isSneaking()){
                setArmorStandPose(as, (getArmorStandPose(as)+1)%13);
                e.getPlayer().swingMainHand();
                e.setCancelled(true);
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
        campfireBoostSoundTracker.remove(e.getPlayer().getUniqueId());

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

            if(damageImmunePlayers.contains(p.getUniqueId())){
                e.setCancelled(true);
                return;
            }

            if(Afk.hasPlayer(p)){
                p.kickPlayer("You took damage will afk\n");
                kicker = ".afk";
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
                Bukkit.getScheduler().runTaskLater(me, new Runnable() {
                    @Override
                    public void run() {
                        p.setVelocity(new Vector(0, 0, 0));
                        p.teleport(respawn); //re-teleport to account for velocity
                        doWorldCrystalRelocateAnim(p);
                        doClientCrystalObscureAnim(p);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacy("Your crystal heart has shattered"));
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
                log.info("[Guhca] Crystal Heart Used.");
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
    public void onPlaceCrystalHeart(BlockPlaceEvent e){
        Material m = e.getBlock().getType();
        if (e.getItemInHand().getItemMeta().hasItemName() &&
                (m.equals(Material.STRUCTURE_BLOCK))){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMonsterSpawn(CreatureSpawnEvent e){
        if(e.getEntity().getSpawnCategory() == SpawnCategory.MONSTER &&
                e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL &&
                !(e.getEntityType() == EntityType.WARDEN) &&
                mobSwitchedWorlds.contains(e.getLocation().getWorld().getUID())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onElytraSneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        if (p.isGliding() && e.isSneaking()){
            p.setGliding(false);
        }
    }

    @EventHandler
    public void onExperienceOrb(PlayerExpCooldownChangeEvent e){
        if(e.getReason() == PlayerExpCooldownChangeEvent.ChangeReason.PICKUP_ORB) {
            e.setNewCooldown(0);
        }
    }

    @EventHandler
    public void onPlaceArmorStand(EntitySpawnEvent e){
        if (e.getEntity() instanceof ArmorStand as){
            setArmorStandPose(as, 0);
            as.setArms(true);
        }
    }

    //@HonouraryEventHandler
    public static void onSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable(){
            public void run() {
                //AFK Tracker
                for(Player p: Bukkit.getOnlinePlayers()){
                    UUID uuid = p.getUniqueId();
                    afkTracker.replace(uuid, (Integer) (afkTracker.get(uuid)+1));

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

    //@HonouraryEventHandler
    public static void onFifteenSec(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(me, new Runnable() {
            @Override
            public void run() {
                for(World w: Bukkit.getWorlds()) {
                    int validZombieVillagers = 0;
                    for (LivingEntity le : w.getLivingEntities()) {
                        if (le.getType() == EntityType.ZOMBIE_VILLAGER) {
                            if (le.getRemoveWhenFarAway()) {
                                validZombieVillagers += 1;
                            }
                        }
                    }

                    String dimension = w.getEnvironment().toString();
                    if (dimension.endsWith("L")) {
                        dimension = "Overworld";
                    }else if(dimension.endsWith("R")){
                        dimension = "ether";
                    }else{
                        dimension = "end";
                    }

                    if (validZombieVillagers > (70 * Bukkit.getOnlinePlayers().size())) {
                        if (mobSwitchedWorlds.add(w.getUID())) {
                            log.info("[Guhca] Mob Switch Enabled for The " + dimension);
                        }
                    } else {
                        if (mobSwitchedWorlds.remove(w.getUID())) {
                            log.info("[Guhca] Mob Switch Disabled for The " + dimension);
                        }
                    }
                }
            }
        }, 0L, 300L);
    }

}