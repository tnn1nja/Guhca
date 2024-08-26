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
    public CommandExec gComm = new CommandExec();
    public TabCompleter gTab = new TabCompleter();
    public static HashSet<String> OfflinePlayers = new HashSet<String>();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, dd/MM/yy");
    public static Logger log = Bukkit.getLogger();
    public static String dataFile;

    //Constants
    public static Integer afkTime = 300;
    public static int lagPing = 1500;

    //Variables
    public static HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();
    public static String kicker = null;
    public static boolean playersDied = false;
    public static HashMap<String, Integer> playerDeaths = new HashMap<String, Integer>();
    public static long survived = -1;
    public static HashSet<UUID> lagTracker = new HashSet<UUID>();


    @Override
    public void onEnable() {
        //Init
        me = this;
        log.info("[Guhca] Guhca Enabled.");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getCommand("lastplayed")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lp")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("kick")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("playtime")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("pt")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("damage")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("deaths")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("kick")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("dimension")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("nightvision")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("nv")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lastplayed")).setTabCompleter(gTab);
        Objects.requireNonNull(getCommand("lp")).setTabCompleter(gTab);
        Objects.requireNonNull(getCommand("dimension")).setTabCompleter(gTab);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Listeners.onSec();
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
            w.setDifficulty(Difficulty.HARD);
        }

        //Check for Deaths
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getStatistic(Statistic.DEATHS) > 0){
                playersDied();
            }
        }

        //Save and Load
        getDataFolder().mkdirs();
        dataFile = getDataFolder() + "/serverData.dat";
        Tools.loadData();
    }


    @Override
    public void onDisable() {
        log.info("[Guhca] Guhca Disabled.");
        Online.unregister();
        Afk.unregister();
        HealthBN.unregister();
        HealthPL.unregister();
        Tools.saveData();
    }
}