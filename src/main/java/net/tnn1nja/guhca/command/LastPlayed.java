package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

public class LastPlayed extends CommandCore {

    @Override
    protected String getName(){
        return "lastplayed";
    }

    @Override
    protected boolean shouldExecute(CommandSender sender, String[] args) {
        if (args.length == 0){
            sender.sendMessage(Component.text("Please specify a player", NamedTextColor.RED));
            return false;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        if (op == null) {
            sender.sendMessage(Component.text(args[0] + " is not a recognised player", NamedTextColor.RED));
            return false;
        }
        if (op.isOnline()) {
            sender.sendMessage(Component.text( op.getName() + " is currently online", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        sender.sendMessage(Component.text(op.getName(), NamedTextColor.RED)
                .append(Component.text(" last played at ", NamedTextColor.WHITE))
                .append(Component.text(getFormattedDate(op.getLastSeen()), NamedTextColor.GOLD)));
    }

    @Override
    protected List<String> getSuggestions(CommandSender sender, String[] args) {
        if (args.length == 1){
            List<String> suggestions = new ArrayList<String>();
            for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
                if(!op.getName().equalsIgnoreCase(sender.getName())) {
                    suggestions.add(op.getName());
                }
            }
            return suggestions;
        }else {
            return none;
        }
    }


    private String getFormattedDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd/MM/yy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        return sdf.format(new Date(millis));
    }

}
