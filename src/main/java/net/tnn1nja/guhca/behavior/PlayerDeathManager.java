package net.tnn1nja.guhca.behavior;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.tnn1nja.guhca.Guhca.guhca;

public class PlayerDeathManager extends BehaviorCore {

    boolean playersDied = false;

    public void playersDied(){
        Bukkit.getServerTickManager().setFrozen(true);
        playersDied = true;
        guhca.log("A player has died, the world is frozen.");
    }

    @Override
    public void onEnable() {
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getStatistic(Statistic.DEATHS) > 0){
                playersDied();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(playersDied){
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        for(Player p: Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SPECTATOR);
        }
        playersDied();
    }

}
