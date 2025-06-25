package net.tnn1nja.guhca.command.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Playtime extends LeaderboardCommand {

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        s.sendMessage(empty());
        sendTitle(s, "Playtime");
        s.sendMessage(text().content("You have collectively survived ").color(WHITE)
                .append(text(Bukkit.getWorlds().get(0).getFullTime()/24000, GOLD))
                .append(text(" days.", WHITE)).build()
        );
        sendLeaderboard(s, Statistic.TOTAL_WORLD_TIME, 72000, "has played for", "hours");
        s.sendMessage(empty());
    }

}
