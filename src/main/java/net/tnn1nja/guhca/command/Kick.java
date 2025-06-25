package net.tnn1nja.guhca.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Kick extends CommandCore {

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        if(args.length == 0) {
            s.sendMessage(text("Please specify a player", RED));
            return;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        if(op == null){
            s.sendMessage(text(args[0] + " is not a recognised player", RED));
            return;
        }
        if(!op.isOnline()) {
            s.sendMessage(text(op.getName() + " is not currently online", RED));
            return;
        }

        Player p = Bukkit.getPlayer(op.getUniqueId());
        Bukkit.broadcast(text(p.getName() + " was kicked by " + s.getName(), GOLD));
        if(args.length > 1) {
            p.kick(text(joinArguments(args, 1)));
        }else {
            p.kick(text("You have been kicked by " + s.getName()));
        }
    }

    @Override
    protected List<String> getSuggestions(CommandSender s, String[] args) {
        if (args.length == 1){
            return onlinePlayers;
        }else{
            return none;
        }
    }

}
