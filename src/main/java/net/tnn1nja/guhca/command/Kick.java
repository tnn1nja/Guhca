package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

import static net.tnn1nja.guhca.Main.OfflinePlayers;
import static net.tnn1nja.guhca.command.CommandUtils.empty;

public class Kick implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length < 1) {
            sender.sendMessage(Component.text("Please specify a player", NamedTextColor.RED));
            return false;
        }

        if(!OfflinePlayers.contains(args[0].toLowerCase())){
            sender.sendMessage(Component.text("Player could not be found", NamedTextColor.RED));
            return false;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0].toLowerCase());
        if(!op.isOnline()){
            sender.sendMessage(Component.text(op.getName() + " is not currently online.",
                    NamedTextColor.RED));
            return false;
        }else{
            Player p = Bukkit.getPlayer(op.getUniqueId());
            if(args.length > 1) {
                p.kick(Component.text(CommandUtils.joinArguments(args, 1, args.length)));
            }else {
                p.kick(Component.text("You have been kicked by " + sender.getName()));
            }
            Bukkit.broadcast(
                    Component.text(p.getName() + " was kicked by " + sender.getName(), NamedTextColor.GOLD)
            );
            return true;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1){
            return empty;
        }else{
            return null;
        }
    }

}
