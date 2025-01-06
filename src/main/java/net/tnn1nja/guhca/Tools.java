package net.tnn1nja.guhca;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.*;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import static net.tnn1nja.guhca.Main.*;

public class Tools {

    @SuppressWarnings("deprecation")
    public static void initScoreboard(){
        //Init
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        //Teams
        for(Team t: board.getTeams()){
            if(t.getName().equalsIgnoreCase("guhcaOnline")){
                t.unregister();
            }else if(t.getName().equalsIgnoreCase("guhcaAFK")){
                t.unregister();
            }
        }
        Online = board.registerNewTeam("guhcaOnline");
        Online.setColor(ChatColor.RED);
        Online.setCanSeeFriendlyInvisibles(false);
        Afk = board.registerNewTeam("guhcaAFK");
        Afk.setColor(ChatColor.GRAY);
        Afk.setCanSeeFriendlyInvisibles(false);

        //Objectives
        for(Objective o: board.getObjectives()) {
            if (o.getName().equalsIgnoreCase("guhcaHealthBN")) {
                o.unregister();
            } else if (o.getName().equalsIgnoreCase("guhcaHealthPL")){
                o.unregister();
            }
        }
        HealthBN = board.registerNewObjective("guhcaHealthBN", "health");
        HealthBN.setDisplayName(ChatColor.DARK_RED + "♥");
        HealthBN.setDisplaySlot(DisplaySlot.BELOW_NAME);
        HealthPL = board.registerNewObjective("guhcaHealthPL", "health");
        HealthPL.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    public static void playersDied(){
        serverFreeze();
        playersDied = true;
        for(Player p: Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    public static void generateOfflinePlayerSet(){
        for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
            OfflinePlayers.add(op.getName().toLowerCase());
        }
    }

    public static void serverFreeze(){
        if(!playersDied) {
            Bukkit.getServerTickManager().setFrozen(true);
            log.info("[Guhca] Server Frozen.");
        }
    }

    public static void delayedServerFreeze(){
        if(!playersDied){
            Bukkit.getServer().getScheduler().runTaskLater(me, new Runnable() {
                @Override
                public void run() {
                    if(Bukkit.getOnlinePlayers().isEmpty()){ //includes leaving player
                        serverFreeze();
                    }
                }
            }, 100L);
        }
    }

    public static void serverResume(){
        if(!playersDied) {
            Bukkit.getServerTickManager().setFrozen(false);
            log.info("[Guhca] Server Resumed.");
        }
    }

    public static String stripMCCodes(String s){
        String bsChar = "\u00A7";
        StringBuilder output = new StringBuilder(s);
        while (output.toString().contains(bsChar)){
            int i = output.indexOf(bsChar);
            output.deleteCharAt(i);
            output.deleteCharAt(i);
        }

        return output.toString();
    }

    public static boolean isRaidersWithinVanillaRange(Location bellLocation){
        Collection<Entity> entities = bellLocation.getWorld().getNearbyEntities(bellLocation,
                vanillaDetectionRadius, vanillaDetectionRadius, vanillaDetectionRadius);
        for(Entity e: entities){
            if(e instanceof Raider && isWithinSphere(e.getLocation(), bellLocation, vanillaDetectionRadius)){
                return true;
            }
        }
        return false;
    }

    public static void startBellCooldown(Location l){
        bellOnCooldownLocations.add(l);
        Bukkit.getScheduler().runTaskLater(me, new Runnable() {
            @Override
            public void run() {
                bellOnCooldownLocations.remove(l);
            }
        }, 60L);
    }

    public static boolean isWithinSphere(Location entityLocation, Location bellLocation, int radius){
        return Math.pow((bellLocation.getX() - entityLocation.getX()), 2) +
                Math.pow((bellLocation.getY() - entityLocation.getY()), 2) +
                Math.pow((bellLocation.getZ() - entityLocation.getZ()), 2)
                <= Math.pow(radius, 2);
    }

    public static Collection<Raider> getRaidersWithinCustomRange(Location bellLocation){
        return bellLocation.getWorld().getNearbyEntities(bellLocation,
                        customDetectionRadius, customDetectionRadius, customDetectionRadius,
                        entity -> entity instanceof Raider &&
                                isWithinSphere(entity.getLocation(), bellLocation, customDetectionRadius)).
                stream().map(entity -> (Raider) entity).toList();
    }

    public static boolean useCrystalHeart(Player p){
        PlayerInventory inv = p.getInventory();
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack item = inv.getItem(i);
            if (item != null &&
                    item.getType() == Material.COMMAND_BLOCK &&
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
                l.setX(l.getX() + r.nextInt(21)-10);
                l.setZ(l.getZ() + r.nextInt(21)-10);
                for(int y = 319; y >= -64; y--){
                    l.setY(y);
                    if(l.getBlock().getType() != Material.AIR) {
                        if (l.getBlock().getType().isSolid()) {
                            l.setY(y+1);
                            return l.add(0.5, 0, 0.5);
                        }else{
                            break;
                        }
                    }
                }
            }
            return Bukkit.getWorlds().get(0).getSpawnLocation().add(0.5, 0, 0.5);
        }
    }

    public static void doCrystalRelocateAnim(Player p){
        World w = Bukkit.getWorlds().get(0);
        w.playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 1F);
        w.spawnParticle(Particle.DUST, p.getLocation().add(0, (int) p.getEyeHeight(),
                        0), 2048, 0.55, 0.55, 0.55, 1,
                new Particle.DustOptions(Color.fromRGB(252, 47, 72), 1.2F), true);
        w.spawnParticle(Particle.DUST, p.getLocation().add(0, p.getEyeHeight(),
                        0), 256, 0.1, 0.1, 0.1, 1,
                new Particle.DustOptions(Color.fromRGB(252, 47, 72), 1.2F), true);
    }

    public static void doCrystalRelocateAnimDelayed(Player p){
        Bukkit.getScheduler().runTaskLater(me, new Runnable() {
            @Override
            public void run() {
                doCrystalRelocateAnim(p);
            }
        }, 1L);
    }

    public static void grantPlayerImmunity(UUID uuid, long ticks){
        damageImmunePlayers.add(uuid);
        Bukkit.getScheduler().runTaskLater(me, new Runnable() {
            @Override
            public void run() {
                damageImmunePlayers.remove(uuid);
            }
        }, ticks);
    }

}
