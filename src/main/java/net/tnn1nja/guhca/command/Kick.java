package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Kick extends CommandCore {

    private String joinArguments(String[] args, int startIndex, int endIndex){
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
    void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Component.text("Please specify a player", NamedTextColor.RED));
            return;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        if(op == null){
            sender.sendMessage(Component.text("Player could not be found", NamedTextColor.RED));
            return;
        }

        if(!op.isOnline()) {
            sender.sendMessage(Component.text("Player is not currently online",
                    NamedTextColor.RED));
            return;
        }

        Player p = Bukkit.getPlayer(op.getUniqueId());
        if(args.length > 1) {
            p.kick(Component.text(joinArguments(args, 1, args.length)));
        }else {
            p.kick(Component.text("You have been kicked by " + sender.getName()));
        }
        Bukkit.broadcast(
                Component.text(p.getName() + " was kicked by " + sender.getName(), NamedTextColor.GOLD)
        );
    }

    @Override
    List<String> suggest(CommandSender sender, String[] args) {
        if (args.length == 1){
            return null;
        }else{
            return empty;
        }
    }

}
