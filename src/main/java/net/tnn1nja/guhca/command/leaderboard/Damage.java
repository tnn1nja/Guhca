package net.tnn1nja.guhca.command.leaderboard;

import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.*;

public class Damage extends LeaderboardCommand {

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        s.sendMessage(text()
                .appendNewline()
                .append(getTitle("Damage Taken"))
                .appendNewline()
                .append(getLeaderboard(Statistic.DAMAGE_TAKEN, 10, "has taken", "damage"))
                .appendNewline().build()
        );
    }

}
