package net.tnn1nja.guhca.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class LastPlayed extends CommandCore {

    @Override
    protected boolean shouldExecute(CommandSender s, String[] args) {
        if (args.length == 0){
            s.sendMessage(text("Please specify a player", RED));
            return false;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        if (op == null) {
            s.sendMessage(text(args[0] + " is not a recognised player", RED));
            return false;
        }
        if (op.isOnline()) {
            s.sendMessage(text( op.getName() + " is currently online", RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0].toLowerCase());
        s.sendMessage(text().content(op.getName()).color(RED)
                .append(text(" last played at ", WHITE))
                .append(text(getFormattedDate(op.getLastSeen()), GOLD)).build());
    }

    @Override
    protected List<String> getSuggestions(CommandSender s, String[] args) {
        if (args.length == 1){
            List<String> suggestions = new ArrayList<>();
            for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
                if(!op.getName().equalsIgnoreCase(s.getName())) {
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
