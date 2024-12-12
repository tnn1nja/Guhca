package net.tnn1nja.guhca;

import net.tnn1nja.guhca.commands.CommandExec;
import net.tnn1nja.guhca.commands.TabCompleter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static net.tnn1nja.guhca.Tools.*;

public final class Main extends JavaPlugin {

    //Globals
    public static Main me;
    public static Team Online;
    public static Team Afk;
    public static Objective HealthBN;
    public static Objective HealthPL;
    public CommandExec ComExec = new CommandExec();
    public TabCompleter TabComp = new TabCompleter();
    public static HashSet<String> OfflinePlayers = new HashSet<String>();
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("hh:mm a, dd/MM/yy");
    public static Logger log = Bukkit.getLogger();

    //Constants
    public static Integer afkTime = 300;
    public static int pingKickThreshold = 5000;
    public static int vanillaDetectionRadius = 32;
    public static int customDetectionRadius = 128;

    //Variables
    public static HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();
    public static HashSet<Location> bellOnCooldownLocations = new HashSet<Location>();
    public static String kicker = null;
    public static boolean playersDied = false;


    @Override
    public void onEnable() {
        //Init
        me = this;
        log.info("[Guhca] Guhca Enabled.");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("lastplayed").setExecutor(ComExec);
        getCommand("kick").setExecutor(ComExec);
        getCommand("playtime").setExecutor(ComExec);
        getCommand("damage").setExecutor(ComExec);
        getCommand("kick").setExecutor(ComExec);
        getCommand("dimension").setExecutor(ComExec);
        getCommand("nightvision").setExecutor(ComExec);
        getCommand("kick").setTabCompleter(TabComp);
        getCommand("lastplayed").setTabCompleter(TabComp);
        getCommand("dimension").setTabCompleter(TabComp);
        DateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Listeners.onSec();
        Listeners.onHalfSec();
        initScoreboard();
        generateOfflinePlayerSet();
        serverFreeze();

        //Kick Players on Reload
        for(Player p: Bukkit.getOnlinePlayers()){
            p.kickPlayer("Server has reloaded.");
        }

        //Set Gamerules
        for(World w: Bukkit.getWorlds()){
            w.setGameRule(GameRule.NATURAL_REGENERATION, false);
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
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

    }


    @Override
    public void onDisable() {
        log.info("[Guhca] Guhca Disabled.");
        Online.unregister();
        Afk.unregister();
        HealthBN.unregister();
        HealthPL.unregister();
    }
}