package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

public class Playtime extends LeaderboardCore {

    @Override
    protected String getName() {
        return "playtime";
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(Component.empty());
        sendTitle(sender, "Playtime");
        sender.sendMessage(Component.text("You have collectively survived ", NamedTextColor.WHITE)
                .append(Component.text(Bukkit.getWorlds().get(0).getFullTime()/24000, NamedTextColor.GOLD))
                .append(Component.text(" days.", NamedTextColor.WHITE))
        );
        sendLeaderboard(sender, Statistic.TOTAL_WORLD_TIME, 72000, "has played for", "hours");
        sender.sendMessage(Component.empty());
    }

}
