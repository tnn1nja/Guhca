package net.tnn1nja.guhca.command.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Leave extends PlayerCommand {

    @Override
    protected void onExecute(Player p, String[] args) {
        if (args.length == 0) {
            Bukkit.broadcast(text(p.getName() + " left the game", GOLD));
            p.kick(text("You have left the game"));
        }else {
            Component message = text(joinArguments(args, 0), GOLD);
            Bukkit.broadcast(message);
            p.kick(text().content("You have left the game for reason:")
                    .appendNewline()
                    .appendNewline()
                    .append(message).build()
            );
        }
    }

    @Override
    protected List<String> getSuggestions(Player p, String[] args) {
        return none;
    }

}
