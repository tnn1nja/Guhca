package net.tnn1nja.guhca;

import net.tnn1nja.guhca.command.CommandCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.logging.Logger;

import static net.tnn1nja.guhca.Tools.*;

public final class Main extends JavaPlugin {

    //Objects
    public static Main plugin;
    public static Team Online;
    public static Team Afk;
    public static Objective HealthName;
    public static Objective HealthList;
    public static Logger log;

    //Constants
    public static Integer afkTime = (Integer) 300;
    public static int pingKickThreshold = 5000;
    public static int vanillaBellDetectionRadius = 32;
    public static int customBellDetectionRadius = 128;
    public static NamespacedKey armorStandDataKey = new NamespacedKey("guhca", "pose_id");
    public static ArmorStandPose[] armorStandPoses = new ArmorStandPose[]{
            //<editor-fold desc="Armor Stand Poses">
            new ArmorStandPose(
                    new double[]{0, 0, 0}, new double[]{0, 0, 0},
                    new double[]{6.11, 0, 6.11}, new double[]{6.02, 0, 0.175},
                    new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
            ),
            new ArmorStandPose(
                    new double[]{0, 0, 0}, new double[]{0, 0, 0},
                    new double[]{0, 0, 0}, new double[]{0, 0, 0},
                    new double[]{0, 0, 0}, new double[]{0, 0, 0}
            ),
            new ArmorStandPose(
                    new double[]{0.262, 0, 0}, new double[]{0, 0, 0.0349},
                    new double[]{5.76, 0.262, 0.262}, new double[]{5.24, 5.93, 6.11},
                    new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
            ),
            new ArmorStandPose(
                    new double[]{6.2, 0, 0}, new double[]{0, 0, 0.0349},
                    new double[]{0.175, 0, 6.2}, new double[]{5.24, 0.349, 6.11},
                    new double[]{6.23, 6.23, 6.23}, new double[]{0.0524, 0.0524, 0.0524}
            ),
            new ArmorStandPose(
                    new double[]{6.02, 0, 0}, new double[]{0, 0, 6.25},
                    new double[]{0.349, 0, 6.11}, new double[]{4.36, 0.873, 0},
                    new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
            ),
            new ArmorStandPose(
                    new double[]{6.02, 0, 0}, new double[]{0, 0, 0},
                    new double[]{4.36, 0.611, 0}, new double[]{4.36, 5.67, 0},
                    new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
            ),
            new ArmorStandPose(
                    new double[]{6.02, 0, 0}, new double[]{0, 0, 0},
                    new double[]{4.36, 5.67, 0}, new double[]{4.36, 0.611, 0},
                    new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
            ),
            new ArmorStandPose(
                    new double[]{0, 0, 0}, new double[]{0, 0, 0},
                    new double[]{0.175, 0, 6.2}, new double[]{5.06, 5.59, 0},
                    new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
            ),
            new ArmorStandPose(
                    new double[]{0.279, 0.349, 0}, new double[]{0, 0, 0},
                    new double[]{0.07, 0.14, 4.14}, new double[]{4.29, 0, 1.55},
                    new double[]{6.04, 5.97, 6.00}, new double[]{0.14, 0.349, 0.07}
            ),
            new ArmorStandPose(
                    new double[]{6.11, 0, 6.2}, new double[]{0, 0, 0},
                    new double[]{4.45, 0, 0}, new double[]{4.54, 0, 0},
                    new double[]{0.122, 0, 0}, new double[]{5.48, 0, 0}
            ),
            new ArmorStandPose(
                    new double[]{6.2, 0.314, 0}, new double[]{0, 0.384, 0},
                    new double[]{0.14, 0, 4.29}, new double[]{0, 1.47, 1.94},
                    new double[]{4.35, 0.96, 0}, new double[]{0, 0.401, 6.06}
            ),
            new ArmorStandPose(
                    new double[]{6.11, 5.93, 0}, new double[]{0, 5.96, 0},
                    new double[]{0, 0, 4.33}, new double[]{0.14, 1.57, 1.94},
                    new double[]{0, 0, 0.227}, new double[]{4.2, 5.55, 0}
            ),
            new ArmorStandPose(
                    new double[]{6.22, 1.17, 0}, new double[]{0, 0.14, 0},
                    new double[]{0.279, 0.559, 6.14}, new double[]{4.56, 1.1, 0},
                    new double[]{0, 4.97, 6.14}, new double[]{0.07, 1.1, 0.14}
            )
            //</editor-fold>
    };

    //Variables
    public static HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();
    public static HashSet<UUID> damageImmunePlayers = new HashSet<UUID>();
    public static HashMap<UUID, Boolean> campfireBoostSoundTracker = new HashMap<>();
    public static HashSet<Location> bellOnCooldownLocations = new HashSet<Location>();
    public static HashSet<UUID> mobSwitchedWorlds = new HashSet<UUID>();
    public static boolean playersDied = false;


    @Override
    public void onEnable() {
        //Init
        plugin = this;
        log = plugin.getLogger();
        log.info("Guhca enabled");
        getServer().getPluginManager().registerEvents(new Listeners(), plugin);
        CommandCore.registerCommands(plugin);

        Listeners.onSec();
        Listeners.onHalfSec();
        Listeners.onFifteenSec();
        initScoreboard();
        reloadDatapack();

        //Set Gamerules
        for(World w: Bukkit.getWorlds()){
            w.setGameRule(GameRule.NATURAL_REGENERATION, false);
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
            w.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
            w.setGameRule(GameRule.DISABLE_PLAYER_MOVEMENT_CHECK, true);
            w.setDifficulty(Difficulty.HARD);
        }

        //Check for Deaths
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getStatistic(Statistic.DEATHS) > 0){
                playersDied();
            }
        }

        //Setup Online Players
        for(Player p: Bukkit.getOnlinePlayers()){
            setupPlayer(p);
        }

    }


    @Override
    public void onDisable() {
        log.info("Guhca disabled");
    }

}