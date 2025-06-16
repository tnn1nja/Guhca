package net.tnn1nja.guhca.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandCore implements CommandExecutor, TabCompleter {

    //Static command registry
    public static void registerCommands(JavaPlugin plugin){
        new Kick().register(plugin);
        new LastPlayed().register(plugin);
        new Leave().register(plugin);
    }


    //Command instance
    public abstract String getName();

    void register(JavaPlugin plugin){
        plugin.getCommand(getName()).setExecutor(this);
        plugin.getCommand(getName()).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, args);
    }
    public abstract boolean execute(CommandSender sender, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return suggest(sender, args);
    }
    public abstract List<String> suggest(CommandSender sender, String[] args);


    //Command utils
    List<String> empty = new ArrayList<String>();

    List<String> filterSuggestion(List<String> input, String arg){
        return input.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).
                collect(Collectors.toList());
    }

}
