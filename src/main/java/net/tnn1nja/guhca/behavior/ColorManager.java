package net.tnn1nja.guhca.behavior;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.text;
import static net.tnn1nja.guhca.Tools.getComponentAsPlainText;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class ColorManager extends BehaviorCore {

    public Team Default;
    public String defaultName = "guhca.default";

    @Override
    public void onEnable() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for(Team t: sb.getTeams()) {
            if (t.getName().equalsIgnoreCase(defaultName)) {
                Default = t;
            }

            if (Default == null) {
                Default = sb.registerNewTeam(defaultName);
                Default.color(RED);
                Default.setCanSeeFriendlyInvisibles(false);
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Default.addEntry(p.getName());
        e.joinMessage(text(getComponentAsPlainText(e.joinMessage()), YELLOW));
        p.displayName(p.name().color(RED));
        p.playerListName(p.name().color(WHITE));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.quitMessage(text(getComponentAsPlainText(e.quitMessage()), GOLD));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        e.deathMessage(text(getComponentAsPlainText(e.deathMessage()), RED));
    }

}
