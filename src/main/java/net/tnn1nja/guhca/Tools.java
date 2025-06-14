package net.tnn1nja.guhca;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.*;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static net.tnn1nja.guhca.Main.*;

public class Tools {

    public static void initScoreboard(){
        //Init
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        //Teams
        for(Team t: board.getTeams()){
            if(t.getName().equalsIgnoreCase("guhca.online")){
                Online = t;
            }else if(t.getName().equalsIgnoreCase("guhca.afk")){
                Afk = t;
            }
        }

        if(Online == null) {
            Online = board.registerNewTeam("guhca.online");
            Online.color(NamedTextColor.RED);
            Online.setCanSeeFriendlyInvisibles(false);
        }
        if(Afk == null){
            Afk = board.registerNewTeam("guhca.afk");
            Afk.color(NamedTextColor.GRAY);
            Afk.setCanSeeFriendlyInvisibles(false);
        }

        //Objectives
        for(Objective o: board.getObjectives()) {
            if (o.getName().equalsIgnoreCase("guhca.health_below_name")) {
                HealthName = o;
            } else if (o.getName().equalsIgnoreCase("guhca.health_player_list")){
                HealthList = o;
            }
        }

        if(HealthName == null){
            HealthName = board.registerNewObjective("guhca.health_below_name", Criteria.HEALTH,
                    Component.text("♥", NamedTextColor.DARK_RED));
            HealthName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        if (HealthList == null){
            HealthList = board.registerNewObjective("guhca.health_player_list", Criteria.HEALTH,
                    Component.text("HealthList"));
            HealthList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
    }

    public static void playersDied(){
        playersDied = true;
        Bukkit.getServerTickManager().setFrozen(true);
        for(Player p: Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SPECTATOR);
        }
        log.info("A player has died, the world is frozen.");
    }

    public static void setupPlayer(Player p){
        //Set Player Data
        p.displayName(p.name().color(NamedTextColor.RED));
        p.playerListName(p.name().color(NamedTextColor.WHITE));
        Online.addEntry(p.getName());
        afkTracker.put(p.getUniqueId(), (Integer) 0);
        campfireBoostSoundTracker.put(p.getUniqueId(), false);

        //Check if Players Died
        if(playersDied){
            p.setGameMode(GameMode.SPECTATOR);
        }

        //Setup New Player
        if(!p.hasPlayedBefore()){
            generateOfflinePlayerSet();
        }

        //Discover all Recipes
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof Keyed){
                p.discoverRecipe(((Keyed) recipe).getKey());
            }
        });
    }

    public static void generateOfflinePlayerSet(){
        for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
            OfflinePlayers.add(op.getName().toLowerCase());
        }
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
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
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
                        customBellDetectionRadius, customBellDetectionRadius, customBellDetectionRadius,
                        entity -> entity instanceof Raider &&
                                isWithinSphere(entity.getLocation(), bellLocation, customBellDetectionRadius)).
                stream().map(entity -> (Raider) entity).toList();
    }

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
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                damageImmunePlayers.remove(uuid);
            }
        }, ticks);
    }

    public static List<String> filterList(List<String> input, String arg){
        return input.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).
                collect(Collectors.toList());
    }

    public static void reloadDatapack(){
        String datapackDir = Bukkit.getWorlds().get(0).getName() + "/datapacks/guhca/";
        String[][] files = {
                {"pack.mcmeta", ""},
                {"reward_ominous_unique.json", "data/minecraft/loot_table/chests/trial_chambers/"}};

        File datapackFile = new File(datapackDir);
        if(datapackFile.exists()){
            log.info("Datapack previously installed, updating...");
            recursiveDelete(datapackFile);
        }

        try {
            for (String[] pair : files) {
                new File(datapackDir + pair[1]).mkdirs();
                InputStream is = Tools.class.getClassLoader().getResourceAsStream("datapack/" + pair[0]);
                FileOutputStream fos = new FileOutputStream(datapackDir + pair[1] + pair[0]);
                is.transferTo(fos);
                is.close();
                fos.close();
            }
            log.info("Datapack installed, reloading data...");
            Bukkit.getServer().reloadData();
        } catch (Exception e) {
            log.severe("Datapack failed to install");
            e.printStackTrace();
        }
    }

    public static void recursiveDelete(File f){
        try{
            if (f.isDirectory()) {
                File[] list = f.listFiles();
                for (File target : list) {
                    recursiveDelete(target);
                }
            }
            f.delete();
        }catch(Exception e){
            log.info("Datapack failed to uninstall");
            e.printStackTrace();
        }
    }

    public static boolean isFullBlock(Block b){
        BoundingBox bb = b.getBoundingBox();
        return (b.getCollisionShape().getBoundingBoxes().size() == 1 &&
                bb.getHeight() == 1.0 &&
                bb.getWidthX() == 1.0 &&
                bb.getWidthZ() == 1.0);

    }

    public static int getArmorStandPose(ArmorStand as){
        PersistentDataContainer pdh = as.getPersistentDataContainer();
        if(pdh.has(armorStandDataKey)) {
            return pdh.get(armorStandDataKey, PersistentDataType.INTEGER);
        }else{
            return 0;
        }
    }

    public static void setArmorStandPose(ArmorStand as, int id){
        as.getPersistentDataContainer().set(armorStandDataKey, PersistentDataType.INTEGER, id);

        ArmorStandPose asp = armorStandPoses[id];
        as.setHeadPose(asp.HEAD_POSE);
        as.setBodyPose(asp.BODY_POSE);
        as.setLeftArmPose(asp.LEFT_ARM_POSE);
        as.setRightArmPose(asp.RIGHT_ARM_POSE);
        as.setLeftLegPose(asp.LEFT_LEG_POSE);
        as.setRightLegPose(asp.RIGHT_LEG_POSE);
    }

    public static String getComponentAsPlainText(Component c){
        return PlainTextComponentSerializer.plainText().serialize(c);
    }

    public static class ArmorStandPose{

        public EulerAngle HEAD_POSE;
        public EulerAngle BODY_POSE;
        public EulerAngle LEFT_ARM_POSE;
        public EulerAngle RIGHT_ARM_POSE;
        public EulerAngle LEFT_LEG_POSE;
        public EulerAngle RIGHT_LEG_POSE;

        public ArmorStandPose(double[] headPose, double[] bodyPose,
                              double[] leftArmPose, double[] rightArmPose,
                              double[] leftLegPose, double[] rightLegPose) {
            try {
                HEAD_POSE = new EulerAngle(headPose[0], headPose[1], headPose[2]);
                BODY_POSE = new EulerAngle(bodyPose[0], bodyPose[1], bodyPose[2]);
                LEFT_ARM_POSE = new EulerAngle(leftArmPose[0], leftArmPose[1], leftArmPose[2]);
                RIGHT_ARM_POSE = new EulerAngle(rightArmPose[0], rightArmPose[1], rightArmPose[2]);
                LEFT_LEG_POSE = new EulerAngle(leftLegPose[0], leftLegPose[1], leftLegPose[2]);
                RIGHT_LEG_POSE = new EulerAngle(rightLegPose[0], rightLegPose[1], rightLegPose[2]);
            }catch(IndexOutOfBoundsException e){
                throw new IllegalArgumentException("All double arrays must be three elements long.");
            }
        }

    }

    public static class PlayerStatHolder {
        public String name;
        public int stat;

        public PlayerStatHolder(String name, int stat){
            this.name = name;
            this.stat = stat;
        }
    }

    public static class PlayerStatHolderComparator implements Comparator<PlayerStatHolder> {
        @Override
        public int compare(PlayerStatHolder one, PlayerStatHolder two) {
            return two.stat-one.stat;
        }

    }

}
