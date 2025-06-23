package net.tnn1nja.guhca.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tnn1nja.guhca.Tools.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CommandExec implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

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