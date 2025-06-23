package net.tnn1nja.guhca.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CommandCore implements CommandExecutor, TabCompleter {

    //Static command register
    public static void registerCommands(JavaPlugin plugin){
        Set<CommandCore> registry = new HashSet<>(List.of(
                new Dimension(),
                new Kick(),
                new LastPlayed(),
                new Leave(),
                new NightVision()
        ));
        for(CommandCore command: registry){
            command.register(plugin);
        }
    }


    //Subclass utilities
    static final List<String> none = new ArrayList<>();
    static final List<String> onlinePlayers = null;

    String joinArguments(String[] args, int startIndex){
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length-1; i++){
            sb.append(args[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


    //Subclass contract and interface bridge
    abstract String getName();
    abstract boolean shouldExecute(CommandSender sender, String[] args);
    abstract void onExecute(CommandSender sender, String[] args);
    abstract List<String> getSuggestions(CommandSender sender, String[] args);

    void register(JavaPlugin plugin){
        plugin.getCommand(getName()).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (shouldExecute(sender, args)) {
            onExecute(sender, args);
        }
        return true; //prevents printing usage
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = getSuggestions(sender, args);
        if(suggestions == onlinePlayers || suggestions == none){
            return suggestions;
        }else {
            return filterSuggestions(suggestions, args[args.length-1]);
        }
    }

    private List<String> filterSuggestions(List<String> suggestions, String arg){
        String lowerArg = arg.toLowerCase();
        List<String> filtered = new ArrayList<>();
        for(String s: suggestions){
            if(s.toLowerCase().startsWith(lowerArg)){
                filtered.add(s);
            }
        }
        return filtered;
    }

}
