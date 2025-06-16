package net.tnn1nja.guhca.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandCore implements CommandExecutor, TabCompleter {

    //Static command registry
    public static void registerCommands(JavaPlugin plugin){
        new Kick().register(plugin);
        new LastPlayed().register(plugin);
        new Leave().register(plugin);
        new Dimension().register(plugin);
    }


    //Command instance
    abstract String getName();

    void register(JavaPlugin plugin){
        plugin.getCommand(getName()).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, args);
    }
    abstract boolean execute(CommandSender sender, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = suggest(sender, args);
        if(result == null || result.isEmpty()){
            return result;
        }else {
            return filterSuggestions(result, args[args.length-1]);
        }
    }
    abstract List<String> suggest(CommandSender sender, String[] args);

    static final List<String> empty = new ArrayList<String>();

    //test this and all commands now
    final List<String> filterSuggestions(List<String> suggestions, String arg){
        String lowerArg = arg.toLowerCase();
        List<String> output = new ArrayList<String>();
        for(String s: suggestions){
            if(s.toLowerCase().startsWith(lowerArg)){
                output.add(s);
            }
        }
        return output;
    }

}
