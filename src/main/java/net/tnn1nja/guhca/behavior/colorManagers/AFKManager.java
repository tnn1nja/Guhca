package net.tnn1nja.guhca.behavior.colorManagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tnn1nja.guhca.behavior.BehaviorCore;
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

public class AFKManager extends BehaviorCore {

    Team AFK;
    String afkTeamName = "guhca.afk";
    Integer afkTime = 300;
    HashMap<UUID, Integer> afkTracker = new HashMap<UUID, Integer>();

    @Override
    public void onEnable(){
        registerRepeatingTask(this::eachSecond, 20);
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        for(Team t: board.getTeams()){
            if(t.getName().equalsIgnoreCase(afkTeamName)){
                AFK = t;
            }
        }

        if(AFK == null){
            AFK = board.registerNewTeam(afkTeamName);
            AFK.color(NamedTextColor.GRAY);
            AFK.setCanSeeFriendlyInvisibles(false);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        afkTracker.put(e.getPlayer().getUniqueId(), 0);
    }

    public void eachSecond(){
        for(Player p: Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();
            afkTracker.replace(uuid, afkTracker.get(uuid)+1);

            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            if(afkTracker.get(uuid) > afkTime && !AFK.equals(sb.getPlayerTeam(p))){
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
            DefaultColorManager.applyDefaultColoring(p);
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
