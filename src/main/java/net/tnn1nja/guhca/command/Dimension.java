package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Dimension extends CommandCore{

    @Override
    public String getName() {
        return "dimension";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player p)){
            sender.sendMessage(Component.text("Only a player can run this command", NamedTextColor.RED));
            return false;
        }

        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            sender.sendMessage(Component.text("Only a spectator to run this command", NamedTextColor.RED));
            return false;
        }

        if(args.length < 1){
            sender.sendMessage(Component.text("You must specify a dimension", NamedTextColor.RED));
            return false;
        }

        switch (args[0]) {
            case "nether": case "n":
                p.teleport(new Location(Bukkit.getWorlds().get(0), 0, 64, 0));
                break;
            case "overworld": case "o":
                p.teleport(new Location(Bukkit.getWorlds().get(1), 0, 150, 0));
                break;
            case "end": case "e":
                p.teleport(new Location(Bukkit.getWorlds().get(2), 0, 100, 0));
                break;
            default:
                sender.sendMessage(Component.text("That dimension is not recognised.", NamedTextColor.RED));
        }
        return true;
    }

    @Override
    public List<String> suggest(CommandSender sender, String[] args) {
        if (args.length == 1){
            return Arrays.asList("overworld", "nether", "end");
        }else{
            return null;
        }
    }

}
