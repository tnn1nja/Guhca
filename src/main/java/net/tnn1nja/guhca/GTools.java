package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

import static net.tnn1nja.guhca.Main.*;

public class GTools {

    @SuppressWarnings("deprecation")
    public static void initScoreboard(){
        //Init
        ScoreboardManager sbm = Objects.requireNonNull(Bukkit.getScoreboardManager());
        Scoreboard board = sbm.getMainScoreboard();

        //Teams
        for(Team t: board.getTeams()){
            if(t.getName().equalsIgnoreCase("guhcaOnline")){
                t.unregister();
            }else if(t.getName().equalsIgnoreCase("guhcaAFK")){
                t.unregister();
            }
        }
        Online = board.registerNewTeam("guhcaOnline");
        Online.setColor(ChatColor.RED);
        Online.setCanSeeFriendlyInvisibles(false);
        Afk = board.registerNewTeam("guhcaAFK");
        Afk.setColor(ChatColor.GRAY);
        Afk.setCanSeeFriendlyInvisibles(false);

        //Objectives
        for(Objective o: board.getObjectives()) {
            if (o.getName().equalsIgnoreCase("guhcaHealthBN")) {
                o.unregister();
            } else if (o.getName().equalsIgnoreCase("guhcaHealthPL")){
                o.unregister();
            }
        }
        HealthBN = board.registerNewObjective("guhcaHealthBN", "health");
        HealthBN.setDisplayName(ChatColor.DARK_RED + "♥");
        HealthBN.setDisplaySlot(DisplaySlot.BELOW_NAME);
        HealthPL = board.registerNewObjective("guhcaHealthPL", "health");
        HealthPL.setDisplaySlot(DisplaySlot.PLAYER_LIST);

    }


    public static void PlayersDied(){
        playersDied = true;
        for(Player p: Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

}
