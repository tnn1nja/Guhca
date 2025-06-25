package net.tnn1nja.guhca.command.leaderboard;

import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.empty;

public class Damage extends LeaderboardCommand {

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        s.sendMessage(empty());
        sendTitle(s, "Damage Taken");
        sendLeaderboard(s, Statistic.DAMAGE_TAKEN, 10, "has taken", "damage");
        s.sendMessage(empty());
    }

}
