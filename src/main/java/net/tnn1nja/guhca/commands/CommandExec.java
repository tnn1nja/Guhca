package net.tnn1nja.guhca.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

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
                            p.kickPlayer(sb.toString());
                        }else{
                            p.kickPlayer("You have been kicked by " + sender.getName());
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

        //Night Vision
        if (command.getName().equalsIgnoreCase("nightvision")) {
            if(playersDied && sender instanceof Player){
                Player p = (Player) sender;
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
            if(playersDied && sender instanceof Player){
                Player p = (Player) sender;
                Location l = p.getLocation();
                World n = Bukkit.getWorld("guhca_nether");
                World e = Bukkit.getWorld("guhca_the_end");
                World o = Bukkit.getWorld("guhca");
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

            //Collective
            long secsSurvived = 0;
            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                int ticks = op.getStatistic(Statistic.TOTAL_WORLD_TIME);
                secsSurvived += ticks/20;
            }

            long hours = secsSurvived / 3600;
            long minutes = (secsSurvived % 3600) / 60;
            String timeSurvived = hours + " hours and " + minutes + " minutes.";

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
            sender.sendMessage("You have collectively survived: " + ChatColor.GOLD + timeSurvived);
            counter = 1;
            for(PlayerStatHolder i: psh){
                hours = i.stat / 3600;
                sender.sendMessage("" + ChatColor.GRAY + counter + ". " + ChatColor.RED + i.name + ChatColor.RESET +
                        " has played for " + ChatColor.GOLD +  hours + ChatColor.RESET + " hours.");
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

    //Player Stat Data Structure
    private class PlayerStatHolder {
        String name;
        int stat;

        public PlayerStatHolder(String name, int stat){
            this.name = name;
            this.stat = stat;
        }
    }

    //Player Stat Comparator
    private static class PlayerStatHolderComparator implements Comparator<PlayerStatHolder> {
        @Override
        public int compare(PlayerStatHolder one, PlayerStatHolder two) {
            return two.stat-one.stat;
        }

    }
}