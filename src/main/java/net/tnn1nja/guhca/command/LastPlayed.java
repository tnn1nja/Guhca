package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

public class LastPlayed extends CommandCore {

    private String getFormattedDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd/MM/yy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        return sdf.format(new Date(millis));
    }

    @Override
    String getName(){
        return "lastplayed";
    }

    @Override
    void execute(CommandSender sender, String[] args) {
        if (args.length < 1){
            sender.sendMessage(Component.text("Please specify a player", NamedTextColor.RED));
            return;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        if (op == null) {
            sender.sendMessage(Component.text("Player could not be found", NamedTextColor.RED));
            return;
        }

        if (op.isOnline()) {
            sender.sendMessage(Component.text(op.getName() + " is currently online",
                    NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text(op.getName(), NamedTextColor.RED)
                .append(Component.text(" last played at ", NamedTextColor.WHITE))
                .append(Component.text(getFormattedDate(op.getLastSeen()), NamedTextColor.GOLD)));
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
