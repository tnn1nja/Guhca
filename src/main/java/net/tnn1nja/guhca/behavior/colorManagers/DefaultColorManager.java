package net.tnn1nja.guhca.behavior.colorManagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.tnn1nja.guhca.behavior.BehaviorCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class DefaultColorManager extends BehaviorCore {

    private static Team DefaultTeam;
    String defaultTeamName = "guhca.default";

    @Override
    public void onEnable() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for(Team t: sb.getTeams()) {
            if (t.getName().equalsIgnoreCase(defaultTeamName)) {
                DefaultTeam = t;
            }

            if (DefaultTeam == null) {
                DefaultTeam = sb.registerNewTeam(defaultTeamName);
                DefaultTeam.color(RED);
                DefaultTeam.setCanSeeFriendlyInvisibles(false);
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.joinMessage(text(getComponentAsPlainText(e.joinMessage()), YELLOW));
        applyDefaultColoring(e.getPlayer());
    }

    public static void applyDefaultColoring(Player p){
        DefaultTeam.addEntry(p.getName());
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

    public static String getComponentAsPlainText(Component c){
        return PlainTextComponentSerializer.plainText().serialize(c);
    }

}
