package net.tnn1nja.guhca.command.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Leave extends PlayerCommand {

    @Override
    protected boolean shouldExecute(Player p, String[] args) {
        return true;
    }

    @Override
    protected void onExecute(Player p, String[] args) {
        if (args.length == 0) {
            p.kick(text("You have left the game"));
            Bukkit.broadcast(text(p.getName() + " left the game", GOLD));
        }else {
            Component message = text(joinArguments(args, 0));
            p.kick(message);
            Bukkit.broadcast(message.color(GOLD));
        }
    }

    @Override
    protected List<String> getSuggestions(Player p, String[] args) {
        return none;
    }

}
