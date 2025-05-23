package net.tnn1nja.guhca.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.tnn1nja.guhca.Tools.filterList;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    List<String> empty = new ArrayList<String>();

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
                return filterList(output, args[0]);
            }

        }

        //Leave
        if(command.getName().equalsIgnoreCase("leave")){
            return empty;
        }

        //Dimension
        if(command.getName().equalsIgnoreCase("dimension")) {
            List<String> output = Arrays.asList("overworld", "nether", "end");

            if (args.length == 1){
                return filterList(output, args[0]);
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
