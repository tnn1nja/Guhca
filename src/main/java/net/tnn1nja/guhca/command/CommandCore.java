package net.tnn1nja.guhca.command;

import net.tnn1nja.guhca.command.leaderboard.Damage;
import net.tnn1nja.guhca.command.leaderboard.Playtime;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
public abstract class CommandCore implements CommandExecutor, TabCompleter {

    //Static command register
    public static void registerCommands(JavaPlugin plugin){
        CommandCore[] registry = new CommandCore[]{
                new Damage(),
                new Dimension(),
                new Kick(),
                new LastPlayed(),
                new Leave(),
                new NightVision(),
                new Playtime()
        };
        for(CommandCore command: registry){
            plugin.getCommand(command.getName()).setExecutor(command);
        }
    }


    //Subclass utilities
    protected static final List<String> none = new ArrayList<>();
    protected static final List<String> onlinePlayers = null;

    protected final String joinArguments(String[] args, int startIndex){
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


    //Subclass contract and interface bridge
    protected abstract boolean shouldExecute(CommandSender sender, String[] args);
    protected abstract void onExecute(CommandSender sender, String[] args);
    protected abstract List<String> getSuggestions(CommandSender sender, String[] args);

    public String getName(){
        return getClass().getName().toLowerCase();
    }

    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (shouldExecute(sender, args)) {
            onExecute(sender, args);
        }
        return true; //prevents printing usage
    }

    public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = getSuggestions(sender, args);
        if(suggestions == null || suggestions.isEmpty()){
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
