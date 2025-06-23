package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

public class Damage extends LeaderboardCommand {

    @Override
    protected String getName() {
        return "damage";
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(
                Component.newline()
                .append(Component.text("-+=", NamedTextColor.GRAY))
                .append(Component.text(" Damage Taken Leaderboard ", NamedTextColor.WHITE))
                .append(Component.text("=+-", NamedTextColor.GRAY))
                .append(getLeaderboardComponent(Statistic.DAMAGE_TAKEN, 10,
                "has taken", "damage"))
        );
    }

}
