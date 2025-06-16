package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

import static net.tnn1nja.guhca.Main.OfflinePlayers;

public class LastPlayed extends CommandCore {

    private static final SimpleDateFormat dateFormat = initDateFormat();
    private static SimpleDateFormat initDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd/MM/yy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        return sdf;
    }

    @Override
    String getName(){
        return "lastplayed";
    }

    @Override
    boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1){
            sender.sendMessage(Component.text("Please specify a player", NamedTextColor.RED));
            return false;
        }

        if (!OfflinePlayers.contains(args[0].toLowerCase())) {
            sender.sendMessage(Component.text("Player could not be found", NamedTextColor.RED));
            return false;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0].toLowerCase());
        if (op.isOnline()) {
            sender.sendMessage(Component.text(op.getName() + " is currently online",
                    NamedTextColor.RED));
            return false;
        }else {
            sender.sendMessage(Component.text(op.getName(), NamedTextColor.RED)
                    .append(Component.text(" last played at ", NamedTextColor.WHITE))
                    .append(Component.text(
                            dateFormat.format(new Date(op.getLastSeen())), NamedTextColor.GOLD)));
            return true;
        }
    }

    @Override
    List<String> suggest(CommandSender sender, String[] args) {
        if (args.length == 1){
            List<String> output = new ArrayList<String>();
            for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
                if(!op.getName().equalsIgnoreCase(sender.getName())) {
                    output.add(op.getName());
                }
            }
            return output;
        }
        return null;
    }

}
