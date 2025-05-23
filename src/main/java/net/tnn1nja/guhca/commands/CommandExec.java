package net.tnn1nja.guhca.commands;

import net.kyori.adventure.text.Component;
import net.tnn1nja.guhca.Tools.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static net.tnn1nja.guhca.Main.*;

public class CommandExec implements CommandExecutor {

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        //Last Played
        if (command.getName().equalsIgnoreCase("lastplayed")) {
            if (args.length > 0) {
                if (OfflinePlayers.contains(args[0].toLowerCase())) {
                    OfflinePlayer pt = Bukkit.getOfflinePlayer(args[0].toLowerCase());
                    if (pt.isOnline()) {
                        sender.sendMessage( ChatColor.RED + pt.getName() + " is currently online.");
                    } else {
                        sender.sendMessage(ChatColor.RED + pt.getName() + ChatColor.WHITE + " Last Played: " +
                                ChatColor.GOLD + DateFormat.format(new Date(pt.getLastPlayed())));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player could not be found.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Please specify a player.");
            }
        }

        //Kick
        if (command.getName().equalsIgnoreCase("kick")){
            if(args.length > 0){
                if(OfflinePlayers.contains(args[0].toLowerCase())){
                    OfflinePlayer pt = Bukkit.getOfflinePlayer(args[0].toLowerCase());
                    if(pt.isOnline()){
                        Player p = Bukkit.getPlayer(pt.getName());
                        kicker = sender.getName();
                        if(args.length > 1) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++){
                                sb.append(args[i]).append(" ");
                            }
                            p.kick(Component.text(sb.toString()));
                        }else{
                            p.kick(Component.text("You have been kicked by " + sender.getName()));
                        }

                    }else{
                        sender.sendMessage(ChatColor.RED + pt.getName() + " is not currently online.");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + args[0] + " has never connected.");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please specify a player.");
            }
        }

        //Leave
        if (command.getName().equalsIgnoreCase("leave")){
            if(sender instanceof Player p) {
                if (args.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : args) {
                        sb.append(s).append(" ");
                    }
                    String msg = sb.toString();
                    kicker = ".self";
                    p.kickPlayer(msg);
                    Bukkit.broadcastMessage(ChatColor.GOLD + msg);
                } else {
                    p.kickPlayer("You have left the game.");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Only a player can run this command.");
            }
        }

        //Night Vision
        if (command.getName().equalsIgnoreCase("nightvision")) {
            if(playersDied && sender instanceof Player p){
                if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    sender.sendMessage("Night vision removed.");
                }else{
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 5));
                    sender.sendMessage("You now have night vision.");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "You cannot use this until you have died.");
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
                            p.sendMessage(ChatColor.RED + "That dimension is not recognised.");
                    }
                }else{
                    p.sendMessage(ChatColor.RED + "You must specify a dimension.");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "You cannot use this until you have died.");
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
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "-+=" + ChatColor.RESET + " Playtime Leaderboard " +
                    ChatColor.GRAY + "=+-");
            sender.sendMessage("You have collectively survived " + ChatColor.GOLD +
                    Bukkit.getWorlds().get(0).getFullTime()/24000 + ChatColor.RESET + " days.");
            counter = 1;
            for(PlayerStatHolder i: psh){
                sender.sendMessage("" + ChatColor.GRAY + counter + ". " + ChatColor.RED + i.name + ChatColor.RESET +
                        " has played for " + ChatColor.GOLD +  i.stat / 3600 + ChatColor.RESET + " hours.");
                counter++;
            }
            sender.sendMessage("");
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
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "-+=" + ChatColor.RESET + " Damage Taken Leaderboard " +
                    ChatColor.GRAY + "=+-");
            int counter = 1;
            for(PlayerStatHolder i: psh){
                sender.sendMessage("" + ChatColor.GRAY + counter + ". " + ChatColor.RED + i.name +  ChatColor.RESET +
                        " has taken " + ChatColor.GOLD + ((float) i.stat)/10 + ChatColor.RESET + " damage.");
                counter++;
            }
            sender.sendMessage("");
        }

        return true;

    }

}