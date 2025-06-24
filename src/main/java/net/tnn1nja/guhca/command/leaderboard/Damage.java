package net.tnn1nja.guhca.command.leaderboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

public class Damage extends LeaderboardCore {

    @Override
    protected String getName() {
        return "damage";
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(Component.empty());
        sendTitle(sender, "Damage Taken");
        sendLeaderboard(sender, Statistic.DAMAGE_TAKEN, 10, "has taken", "damage");
        sender.sendMessage(Component.empty());
    }

}
