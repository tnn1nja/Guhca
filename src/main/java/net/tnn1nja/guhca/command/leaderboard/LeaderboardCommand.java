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

public abstract class LeaderboardCommand extends CommandCore {

    /*
    - this method needs general clean up
    - getLeaderboardComponent should return without a new line at the end and
      the line should be in subclasses
    - components should use builders
    - consider general inheritence structure of CommandCore and subclasses
    */

    //CommandCore implementations
    @Override protected boolean shouldExecute(CommandSender sender, String[] args) { return true; }
    @Override protected List<String> getSuggestions(CommandSender sender, String[] args) { return none; }


    //Create leaderboards
    private OfflinePlayer[] getOfflinePlayersSortedByStatistic(Statistic statistic){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        Arrays.sort(offlinePlayers, Comparator.comparingInt(x -> x.getStatistic(statistic)));
        return offlinePlayers;
    }

    protected Component getLeaderboardComponent(Statistic statistic, int statDivider, String connector, String unit){
        Component leaderboard = Component.empty();
        int i = 1;
        OfflinePlayer[] sortedPlayers = getOfflinePlayersSortedByStatistic(statistic);
        for (OfflinePlayer op : sortedPlayers) {
            leaderboard = leaderboard.append(Component.text(i + ". ", NamedTextColor.GRAY)
                    .append(Component.text(op.getName(), NamedTextColor.RED))
                    .append(Component.text(" " + connector + " ", NamedTextColor.WHITE))
                    .append(Component.text(op.getStatistic(statistic)/statDivider,
                            NamedTextColor.GOLD))
                    .append(Component.text(" " + unit + ".", NamedTextColor.WHITE))
                    .append(Component.newline())
            );
            i++;
        }
        return leaderboard;
    }

}
