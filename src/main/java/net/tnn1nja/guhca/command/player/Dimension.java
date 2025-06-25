package net.tnn1nja.guhca.command.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Dimension extends PlayerCommand {

    @Override
    protected void onExecute(Player p, String[] args) {
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            p.sendMessage(text("Only a spectator to run this command", RED));
            return;
        }
        if(args.length == 0){
            p.sendMessage(text("You must specify a dimension", RED));
            return;
        }

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
    protected List<String> getSuggestions(Player p, String[] args) {
        if (args.length == 1){
            return List.of("overworld", "nether", "end");
        }else{
            return none;
        }
    }

}
