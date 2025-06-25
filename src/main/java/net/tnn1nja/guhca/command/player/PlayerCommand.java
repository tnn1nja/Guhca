package net.tnn1nja.guhca.command.player;

import net.tnn1nja.guhca.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public abstract class PlayerCommand extends Command {

    protected abstract boolean shouldExecute(Player p, String[] args);
    protected abstract void onExecute(Player p, String[] args);
    protected abstract List<String> getSuggestions(Player p, String[] args);

    protected boolean shouldExecute(CommandSender s, String[] args){
        if(!(s instanceof Player p)){
            s.sendMessage(text("Only a player can run this command", RED));
            return false;
        }else{
            return shouldExecute(p, args);
        }
    }

    protected void onExecute(CommandSender s, String[] args){
        onExecute((Player) s, args);
    }

    protected List<String> getSuggestions(CommandSender s, String[] args){
        if(!(s instanceof Player p)){
            return none;
        }else{
            return getSuggestions(p, args);
        }
    }

}
