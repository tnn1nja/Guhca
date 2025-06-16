package net.tnn1nja.guhca.command;

import net.tnn1nja.guhca.commands.CommandExec;
import net.tnn1nja.guhca.commands.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.tnn1nja.guhca.Main.plugin;

public class CommandUtils {

    public static List<String> empty = new ArrayList<String>();
    public static CommandExec ComExec = new CommandExec();
    public static TabCompleter TabComp = new TabCompleter();

    public static void registerCommands(){

        LastPlayed lastPlayed = new LastPlayed();
        plugin.getCommand("lastplayed").setExecutor(lastPlayed);
        plugin.getCommand("lastplayed").setTabCompleter(lastPlayed);

        Kick kick = new Kick();
        plugin.getCommand("kick").setExecutor(kick);
        plugin.getCommand("kick").setTabCompleter(kick);

        Leave leave = new Leave();
        plugin.getCommand("leave").setExecutor(leave);
        plugin.getCommand("leave").setExecutor(leave);

        plugin.getCommand("playtime").setExecutor(ComExec);
        plugin.getCommand("damage").setExecutor(ComExec);
        plugin.getCommand("dimension").setExecutor(ComExec);
        plugin.getCommand("nightvision").setExecutor(ComExec);
        plugin.getCommand("dimension").setTabCompleter(TabComp);
        plugin.getCommand("nightvision").setTabCompleter(TabComp);

    }

    public static List<String> filterSuggestion(List<String> input, String arg){
        return input.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).
                collect(Collectors.toList());
    }

    public static String joinArguments(String[] args, int startIndex, int endIndex){
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++){
            sb.append(args[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

}
