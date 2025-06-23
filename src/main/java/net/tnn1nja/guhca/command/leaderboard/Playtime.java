package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

public class Playtime extends LeaderboardCommand {

    @Override
    protected String getName() {
        return "playtime";
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(
                Component.newline()
                .append(Component.text("-+=", NamedTextColor.GRAY))
                .append(Component.text(" Playtime Leaderboard ", NamedTextColor.WHITE))
                .append(Component.text("=+-", NamedTextColor.GRAY))
                .append(Component.newline())
                .append(Component.text("You have collectively survived ", NamedTextColor.WHITE))
                .append(Component.text(Bukkit.getWorlds().get(0).getFullTime()/24000, NamedTextColor.GOLD))
                .append(Component.text(" days.", NamedTextColor.WHITE))
                .append(getLeaderboardComponent(Statistic.TOTAL_WORLD_TIME, 72000,
                        "has played for", "hours"))
        );
    }

}
