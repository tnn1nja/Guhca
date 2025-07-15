package net.tnn1nja.guhca;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static net.tnn1nja.guhca.Tools.*;

public final class Main extends JavaPlugin {

    public static HashSet<UUID> damageImmunePlayers = new HashSet<UUID>();
    public static boolean playersDied = false;


    @Override
    public void onEnable() {
        initScoreboard();
        reloadDatapack();

        //Check for Deaths
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getStatistic(Statistic.DEATHS) > 0){
                playersDied();
            }
        }

    }

}