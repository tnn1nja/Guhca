package net.tnn1nja.guhca.command.player;

import net.tnn1nja.guhca.command.CommandCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public abstract class PlayerCommand extends CommandCore {

    protected abstract void onExecute(Player p, String[] args);
    protected abstract List<String> getSuggestions(Player p, String[] args);

    protected final void onExecute(CommandSender s, String[] args){
        if(!(s instanceof Player p)){
            s.sendMessage(text("Only a player can run this command", RED));
            return;
        }
        onExecute(p, args);
    }

    protected final List<String> getSuggestions(CommandSender s, String[] args){
        if(!(s instanceof Player p)){
            return none; //don't provide suggestions to none players
        }
        return getSuggestions(p, args);
    }

}
