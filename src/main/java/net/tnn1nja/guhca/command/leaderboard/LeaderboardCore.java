package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tnn1nja.guhca.command.CommandCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class LeaderboardCore extends CommandCore {

    //CommandCore implementations
    @Override protected boolean shouldExecute(CommandSender sender, String[] args) { return true; }
    @Override protected List<String> getSuggestions(CommandSender sender, String[] args) { return none; }


    //Create and send leaderboards
    private OfflinePlayer[] getOfflinePlayersSortedByStatistic(Statistic stat){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        Arrays.sort(offlinePlayers, Comparator.comparingInt(x -> x.getStatistic(stat)));
        return offlinePlayers;
    }

    protected void sendTitle(CommandSender sender, String title){
        sender.sendMessage(Component.text("-+= ").color(NamedTextColor.GRAY)
                .append(Component.text(title + " Leaderboard", NamedTextColor.WHITE))
                .append(Component.text(" =+-", NamedTextColor.GRAY)));
    }

    protected void sendLeaderboard(CommandSender sender, Statistic stat, int divisor, String connector, String unit){
        int i = 1;
        OfflinePlayer[] sortedPlayers = getOfflinePlayersSortedByStatistic(stat);
        for (OfflinePlayer op : sortedPlayers) {
            sender.sendMessage(Component.text(i + ". ", NamedTextColor.GRAY)
                    .append(Component.text(op.getName(), NamedTextColor.RED))
                    .append(Component.text(" " + connector + " ", NamedTextColor.WHITE))
                    .append(Component.text(op.getStatistic(stat)/divisor, NamedTextColor.GOLD))
                    .append(Component.text(" " + unit + ".", NamedTextColor.WHITE))
            );
            i++;
        }
    }

}
