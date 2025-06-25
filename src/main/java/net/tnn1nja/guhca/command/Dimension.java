package net.tnn1nja.guhca.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Dimension extends Command {

    @Override
    protected boolean shouldExecute(CommandSender s, String[] args) {
        if(!(s instanceof Player p)){
            s.sendMessage(text("Only a player can run this command", RED));
            return false;
        }
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            p.sendMessage(text("Only a spectator to run this command", RED));
            return false;
        }
        if(args.length == 0){
            p.sendMessage(text("You must specify a dimension", RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        Player p = (Player) s;
        switch (args[0]) {
            case "overworld":
                p.teleport(new Location(Bukkit.getWorlds().get(0), 0, 64, 0));
                break;
            case "nether":
                p.teleport(new Location(Bukkit.getWorlds().get(1), 0, 150, 0));
                break;
            case "end":
                p.teleport(new Location(Bukkit.getWorlds().get(2), 0, 100, 0));
                break;
            default:
                p.sendMessage(text("That dimension is not recognised", RED));
        }
    }

    @Override
    protected List<String> getSuggestions(CommandSender s, String[] args) {
        if (args.length == 1){
            return List.of("overworld", "nether", "end");
        }else{
            return none;
        }
    }

}
