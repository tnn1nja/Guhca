package net.tnn1nja.guhca;

import net.tnn1nja.guhca.commands.GCommandExec;
import net.tnn1nja.guhca.commands.GTabCompleter;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;


import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static net.tnn1nja.guhca.GTools.PlayersDied;
import static net.tnn1nja.guhca.GTools.initScoreboard;

public final class Main extends JavaPlugin {

    //Globals
    public static Main me;
    public static Team Online;
    public static Team Afk;
    public static Objective HealthBN;
    public static Objective HealthPL;
    public static boolean playersDied = false;
    public GCommandExec gComm = new GCommandExec();
    public GTabCompleter gTab = new GTabCompleter();
    public static boolean kicked = false;

    //Constants
    public static Integer afkTime = 300;

    //Variables
    public static HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();


    @Override
    public void onEnable() {
        //Init
        me = this;
        getLogger().info("Guhca Enabled.");
        getServer().getPluginManager().registerEvents(new GListener(), this);
        Objects.requireNonNull(getCommand("lastplayed")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lp")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("kick")).setExecutor(gComm);
        Objects.requireNonNull(getCommand("lp")).setTabCompleter(gTab);
        Objects.requireNonNull(getCommand("lastplayed")).setTabCompleter(gTab);
        GListener.onSec();
        initScoreboard();

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
        Bukkit.getLogger().info("Guhca Disabled.");
        Online.unregister();
        Afk.unregister();
        HealthBN.unregister();
        HealthPL.unregister();
    }
}