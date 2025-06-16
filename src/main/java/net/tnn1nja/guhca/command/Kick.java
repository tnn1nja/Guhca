package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.tnn1nja.guhca.Main.OfflinePlayers;

public class Kick extends CommandCore {

    String joinArguments(String[] args, int startIndex, int endIndex){
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++){
            sb.append(args[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    String getName(){
        return "kick";
    }

    @Override
    boolean execute(CommandSender sender, String[] args) {
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
            sender.sendMessage(Component.text(op.getName() + " is not currently online",
                    NamedTextColor.RED));
            return false;
        }else{
            Player p = Bukkit.getPlayer(op.getUniqueId());
            if(args.length > 1) {
                p.kick(Component.text(joinArguments(args, 1, args.length)));
            }else {
                p.kick(Component.text("You have been kicked by " + sender.getName()));
            }
            Bukkit.broadcast(
                    Component.text(p.getName() + " was kicked by " + sender.getName(), NamedTextColor.GOLD)
            );
            return true;
        }
    }

    @Override
    List<String> suggest(CommandSender sender, String[] args) {
        if (args.length > 1){
            return empty;
        }else{
            return null;
        }
    }

}
