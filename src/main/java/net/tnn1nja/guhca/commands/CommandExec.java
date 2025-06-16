package net.tnn1nja.guhca.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tnn1nja.guhca.Tools.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

import static net.tnn1nja.guhca.Main.*;

public class CommandExec implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        //Night Vision
        if (command.getName().equalsIgnoreCase("nightvision")) {
            if(playersDied && sender instanceof Player p){
                if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    sender.sendMessage(Component.text("Night vision removed."));
                }else{
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 5));
                    sender.sendMessage(Component.text("You now have night vision."));
                }
            }else{
                sender.sendMessage(Component.text("You cannot use this until you have died.",
                        NamedTextColor.RED));
            }
        }

        //Dimension
        if (command.getName().equalsIgnoreCase("dimension")) {
            if(playersDied && sender instanceof Player p){
                Location l = p.getLocation();
                List<World> worlds = Bukkit.getWorlds();
                World o = worlds.get(0);
                World n = worlds.get(1);
                World e = worlds.get(2);
                Location tpl;
                if (args.length > 0){
                    switch(args[0]){
                        case "nether": case "n":
                            tpl = new Location(n, 0, 64, 0);
                            p.teleport(tpl);
                            break;
                        case "overworld": case "o":
                            tpl = new Location(o, 0, 150, 0);
                            p.teleport(tpl);
                            break;
                        case "end": case "e":
                            tpl = new Location(e, 0, 100, 0);
                            p.teleport(tpl);
                            break;
                        default:
                            p.sendMessage(Component.text("That dimension is not recognised.",
                                    NamedTextColor.RED));
                    }
                }else{
                    p.sendMessage(Component.text("You must specify a dimension.", NamedTextColor.RED));
                }
            }else{
                sender.sendMessage(Component.text("You cannot use this until you have died.",
                        NamedTextColor.RED));
            }
        }

        //Playtime
        if (command.getName().equalsIgnoreCase("playtime")) {

            //Extract
            PlayerStatHolder[] psh = new PlayerStatHolder[Bukkit.getOfflinePlayers().length];
            int counter = 0;
            for(int i = 0; i < Bukkit.getOfflinePlayers().length; i++){
                OfflinePlayer op = Bukkit.getOfflinePlayers()[i];
                psh[i] = new PlayerStatHolder(op.getName(), op.getStatistic(Statistic.TOTAL_WORLD_TIME)/20);
            }

            //Format
            Arrays.sort(psh, new PlayerStatHolderComparator());

            //Output
            sender.sendMessage(Component.newline()
                .append(Component.text("-+=", NamedTextColor.GRAY))
                .append(Component.text(" Playtime Leaderboard ", NamedTextColor.WHITE))
                .append(Component.text("=+-", NamedTextColor.GRAY))
                .append(Component.newline())
                .append(Component.text("You have collectively survived ", NamedTextColor.WHITE))
                .append(Component.text(Bukkit.getWorlds().get(0).getFullTime()/24000, NamedTextColor.GOLD))
                .append(Component.text(" days.", NamedTextColor.WHITE))
            );
            counter = 1;
            for(PlayerStatHolder i: psh){
                sender.sendMessage(Component.text(counter + ". ", NamedTextColor.GRAY)
                    .append(Component.text(i.name, NamedTextColor.RED))
                    .append(Component.text(" has played for ", NamedTextColor.WHITE))
                    .append(Component.text(i.stat/3600, NamedTextColor.GOLD))
                    .append(Component.text(" hours.", NamedTextColor.WHITE))
                );
                counter++;
            }
            sender.sendMessage(Component.empty());
        }

        //DamageLeaderboard
        if (command.getName().equalsIgnoreCase("damage")) {

            //Extract
            PlayerStatHolder[] psh = new PlayerStatHolder[Bukkit.getOfflinePlayers().length];
            for(int i = 0; i<Bukkit.getOfflinePlayers().length; i++){
                OfflinePlayer op = Bukkit.getOfflinePlayers()[i];
                psh[i] = new PlayerStatHolder(op.getName(), op.getStatistic(Statistic.DAMAGE_TAKEN));
            }

            //Format
            Arrays.sort(psh, new PlayerStatHolderComparator());

            //Output
            sender.sendMessage(Component.newline()
                .append(Component.text("-+=", NamedTextColor.GRAY))
                .append(Component.text(" Damage Taken Leaderboard ", NamedTextColor.WHITE))
                .append(Component.text("=+-", NamedTextColor.GRAY))
            );
            int counter = 1;
            for(PlayerStatHolder i: psh){
                sender.sendMessage(Component.text(counter + ". ", NamedTextColor.GRAY)
                    .append(Component.text(i.name, NamedTextColor.RED))
                    .append(Component.text(" has taken ", NamedTextColor.WHITE))
                    .append(Component.text(((float) i.stat)/10, NamedTextColor.GOLD))
                    .append(Component.text(" damage.", NamedTextColor.WHITE))
                );
                counter++;
            }
            sender.sendMessage("");
        }

        return true;

    }

}