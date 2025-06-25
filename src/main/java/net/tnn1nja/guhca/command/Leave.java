package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class Leave extends CommandCore {

    @Override
    protected boolean shouldExecute(CommandSender s, String[] args) {
        if(!(s instanceof Player)) {
            s.sendMessage(text("Only a player can run this command", RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        Player p = (Player) s;
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
    protected List<String> getSuggestions(CommandSender s, String[] args) {
        return none;
    }

}
