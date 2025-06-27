package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.tnn1nja.guhca.command.CommandCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public abstract class LeaderboardCommand extends CommandCore {

    //CommandCore implementations
    @Override protected List<String> getSuggestions(CommandSender s, String[] args) { return none; }


    //Create and send leaderboards
    protected Component getTitle(String title){
        return text().content("-+= ").color(GRAY)
                .append(text(title + " Leaderboard", WHITE))
                .append(text(" =+-", GRAY)).build();
    }

    protected Component getLeaderboard(Statistic stat, int divisor, String connector, String unit){
        TextComponent.Builder leaderboard = Component.text();
        OfflinePlayer[] sortedPlayers = getOfflinePlayersSortedByStatistic(stat);
        for(int i = 0; i < sortedPlayers.length; i++){
            leaderboard.append(text(i+1 + ". ", GRAY))
                    .append(text(sortedPlayers[i].getName(), RED))
                    .append(text(" " + connector + " ", WHITE))
                    .append(text(sortedPlayers[i].getStatistic(stat)/divisor, GOLD))
                    .append(text(" " + unit, WHITE));
            if(i != sortedPlayers.length-1){ //not on last iteration
                leaderboard.appendNewline();
            }
        }
        return leaderboard.build();
    }

    private OfflinePlayer[] getOfflinePlayersSortedByStatistic(Statistic stat){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        Arrays.sort(offlinePlayers, (x, y) -> y.getStatistic(stat) - x.getStatistic(stat));
        return offlinePlayers;
    }


}
