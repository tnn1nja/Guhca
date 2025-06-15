package net.tnn1nja.guhca.command;

import io.papermc.paper.command.brigadier.BasicCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.tnn1nja.guhca.Main.plugin;

@SuppressWarnings("UnstableApiUsage")
public abstract class CommandCore implements BasicCommand{

    //Template
    String name;
    String description;
    Collection<String> aliases;

    public void register(){
        plugin.registerCommand(name, description, aliases, this);
    }

    //Utils
    List<String> filterSuggestion(List<String> input, String arg){
        return input.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).
                collect(Collectors.toList());
    }

    List<String> empty = new ArrayList<String>();


    //Register Commands
    public static void registerCommands(){
        new Test().register();
    }
}
