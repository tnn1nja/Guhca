package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class Leave implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player p)) {
            sender.sendMessage(Component.text("Only a player can run this command", NamedTextColor.RED));
            return false;
        }

        if (args.length > 0) {
            Component message = Component.text(String.join(" ", args));
            p.kick(message);
            Bukkit.broadcast(message.color(NamedTextColor.GOLD));
        } else {
            p.kick(Component.text("You have left the game"));
            Bukkit.broadcast(Component.text(p.getName() + " left the game", NamedTextColor.GOLD));
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return CommandUtils.empty;
    }
}
