package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Dimension extends CommandCore {

    @Override
    protected boolean shouldExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player p)){
            sender.sendMessage(Component.text("Only a player can run this command", NamedTextColor.RED));
            return false;
        }
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            p.sendMessage(Component.text("Only a spectator to run this command", NamedTextColor.RED));
            return false;
        }
        if(args.length == 0){
            p.sendMessage(Component.text("You must specify a dimension", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
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
                p.sendMessage(Component.text("That dimension is not recognised", NamedTextColor.RED));
        }
    }

    @Override
    protected List<String> getSuggestions(CommandSender sender, String[] args) {
        if (args.length == 1){
            return List.of("overworld", "nether", "end");
        }else{
            return none;
        }
    }

}
