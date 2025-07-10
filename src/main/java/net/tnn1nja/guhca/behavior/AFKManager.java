package net.tnn1nja.guhca.behavior;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public class AFKManager extends BehaviorCore{

    public Team AFK;
    public Integer afkTime = 300;
    public HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();
    public Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @Override
    public void onEnable(){
        registerRepeatingTask(this::eachSecond, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        afkTracker.put(e.getPlayer().getUniqueId(), 0);
    }

    public void eachSecond(){
        for(Player p: Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();
            afkTracker.replace(uuid, afkTracker.get(uuid)+1);

            if(afkTracker.get(uuid) > afkTime && !AFK.equals(scoreboard.getPlayerTeam(p))){
                AFK.addEntry(p.getName());
                p.playerListName(p.name().color(GRAY).decorate(ITALIC));
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        afkTracker.replace(p.getUniqueId(), 0);
        if(AFK.getEntries().contains(p.getName())) {
            Default.addEntry(p.getName());
            p.playerListName(p.name().color(WHITE));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        afkTracker.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player p){
            if(AFK.hasPlayer(p)){
                Bukkit.broadcast(
                        Component.text(p.getName() + " took damage while afk", GOLD)
                );
                p.kick(Component.text("You took damage will afk\n"));
            }
        }
    }

}
