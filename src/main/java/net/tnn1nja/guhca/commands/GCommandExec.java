package net.tnn1nja.guhca.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static net.tnn1nja.guhca.Main.*;

public class GCommandExec implements CommandExecutor {

    public static ArrayList<String> OfflinePlayers = new ArrayList<String>();
    static{
        for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
            OfflinePlayers.add(op.getName().toLowerCase());
        }
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, dd/MM/yy");
    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("lastplayed") ||
                command.getName().equalsIgnoreCase("lp")) {
            if (args.length > 0) {
                if (OfflinePlayers.contains(args[0].toLowerCase())) {
                    OfflinePlayer pt = Bukkit.getOfflinePlayer(args[0].toLowerCase());
                    if (pt.isOnline()) {
                        sender.sendMessage( ChatColor.RED + pt.getName() + " is currently online.");
                    } else {
                        if (pt.hasPlayedBefore()) {
                            sender.sendMessage(ChatColor.RED + pt.getName() + ChatColor.WHITE + " Last Played: " +
                                            ChatColor.GOLD + dateFormat.format(new Date(pt.getLastPlayed())));
                        }else{
                            sender.sendMessage(ChatColor.RED + "Player has never joined.");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player could not be found.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Please specify a player.");
            }
        }

        if (command.getName().equalsIgnoreCase("kick")){
            if(args.length > 0){
                if(OfflinePlayers.contains(args[0].toLowerCase())){
                    OfflinePlayer pt = Bukkit.getOfflinePlayer(args[0].toLowerCase());
                    if(pt.isOnline()){
                        Player p = Bukkit.getPlayer(pt.getName());
                        p.kickPlayer("You have been kicked by " + sender.getName());
                        kicked = true;

                    }else{
                        sender.sendMessage(ChatColor.RED + pt.getName() + " is not currently online.");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please specify a player.");
            }
        }

        return true;
    }
}
