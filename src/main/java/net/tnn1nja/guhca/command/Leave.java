package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Leave extends CommandCore {

    @Override
    protected boolean shouldExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only a player can run this command", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.kick(Component.text("You have left the game"));
            Bukkit.broadcast(Component.text(p.getName() + " left the game", NamedTextColor.GOLD));
        }else {
            Component message = Component.text(joinArguments(args, 0));
            p.kick(message);
            Bukkit.broadcast(message.color(NamedTextColor.GOLD));
        }
    }

    @Override
    protected List<String> getSuggestions(CommandSender sender, String[] args) {
        return none;
    }

}
