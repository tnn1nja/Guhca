package net.tnn1nja.guhca;

import net.tnn1nja.guhca.commands.GCommandExec;
import net.tnn1nja.guhca.commands.GTabCompleter;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static net.tnn1nja.guhca.GTools.*;

public final class Main extends JavaPlugin {

    //Globals
    public static Main me;
    public static Team Online;
    public static Team Afk;
    public static Objective HealthBN;
    public static Objective HealthPL;
    public GCommandExec gComm = new GCommandExec();
    public GTabCompleter gTab = new GTabCompleter();
    public static ArrayList<String> OfflinePlayers = new ArrayList<String>();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, dd/MM/yy");
    public static Logger log = Bukkit.getLogger();

    //Constants
    public static Integer afkTime = 300;

    //Variables
    public static HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();
    public static boolean kicked = false;
    public static boolean playersDied = false;


    @Override
    public void onEnable() {
        //Init
        me = this;
        log.info("[Gucha] Guhca Enabled.");
        getServer().getPluginManager().registerEvents(new GListener(), this);
        Objects.requireNonNull(getCommand("lastplayed")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lp")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("kick")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("playtime")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("pt")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lp")).setTabCompleter(gTab);
        Objects.requireNonNull(getCommand("lastplayed")).setTabCompleter(gTab);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        GListener.onSec();
        initScoreboard();
        OFPGen();

        //Set Gamerules
        for(World w: Bukkit.getWorlds()){
            w.setGameRule(GameRule.NATURAL_REGENERATION, false);
            w.setDifficulty(Difficulty.HARD);
        }

        //Check for Deaths
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getStatistic(Statistic.DEATHS) > 0){
                PlayersDied();
            }
        }
    }


    @Override
    public void onDisable() {
        log.info("[Gucha] Guhca Disabled.");
        Online.unregister();
        Afk.unregister();
        HealthBN.unregister();
        HealthPL.unregister();
    }
}