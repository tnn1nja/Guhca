package net.tnn1nja.guhca.command.leaderboard;

import net.tnn1nja.guhca.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public abstract class LeaderboardCommand extends Command {

    //CommandCore implementations
    @Override protected boolean shouldExecute(CommandSender s, String[] args) { return true; }
    @Override protected List<String> getSuggestions(CommandSender s, String[] args) { return none; }


    //Create and send leaderboards
    protected void sendTitle(CommandSender sender, String title){
        sender.sendMessage(text("-+= ").color(GRAY)
                .append(text(title + " Leaderboard", WHITE))
                .append(text(" =+-", GRAY)));
    }

    protected void sendLeaderboard(CommandSender sender, Statistic stat, int divisor, String connector, String unit){
        int i = 1;
        OfflinePlayer[] sortedPlayers = getOfflinePlayersSortedByStatistic(stat);
        for (OfflinePlayer op : sortedPlayers) {
            sender.sendMessage(text(i + ". ", GRAY)
                    .append(text(op.getName(), RED))
                    .append(text(" " + connector + " ", WHITE))
                    .append(text(op.getStatistic(stat)/divisor, GOLD))
                    .append(text(" " + unit + ".", WHITE))
            );
            i++;
        }
    }

    private OfflinePlayer[] getOfflinePlayersSortedByStatistic(Statistic stat){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        Arrays.sort(offlinePlayers, Comparator.comparingInt(x -> x.getStatistic(stat)));
        return offlinePlayers;
    }


}
