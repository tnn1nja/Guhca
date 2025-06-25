package net.tnn1nja.guhca.command.leaderboard;

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
    protected void sendTitle(CommandSender s, String title){
        s.sendMessage(text().content("-+= ").color(GRAY)
                .append(text(title + " Leaderboard", WHITE))
                .append(text(" =+-", GRAY)).build()
        );
    }

    protected void sendLeaderboard(CommandSender s, Statistic stat, int divisor, String connector, String unit){
        int i = 1;
        OfflinePlayer[] sortedPlayers = getOfflinePlayersSortedByStatistic(stat);
        for (OfflinePlayer op : sortedPlayers) {
            s.sendMessage(text().content(i + ". ").color(GRAY)
                    .append(text(op.getName(), RED))
                    .append(text(" " + connector + " ", WHITE))
                    .append(text(op.getStatistic(stat)/divisor, GOLD))
                    .append(text(" " + unit + ".", WHITE)).build()
            );
            i++;
        }
    }

    private OfflinePlayer[] getOfflinePlayersSortedByStatistic(Statistic stat){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        Arrays.sort(offlinePlayers, (x, y) -> y.getStatistic(stat) - x.getStatistic(stat));
        return offlinePlayers;
    }


}
