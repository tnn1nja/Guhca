package net.tnn1nja.guhca.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    ArrayList<String> empty = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        //Last Played
        if(command.getName().equalsIgnoreCase("lastplayed")) {

            List<String> output = new ArrayList<String>();
            for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
                if(!op.getName().equalsIgnoreCase(sender.getName())) {
                    output.add(op.getName());
                }
            }

            if (args.length == 1){
                return output.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).
                        collect(Collectors.toList());
            }
        }

        //Kick
        if(command.getName().equalsIgnoreCase("kick")) {
            if (args.length > 1){
                return empty;
            }
        }

        return null;
    }
}
