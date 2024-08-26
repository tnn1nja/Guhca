package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static net.tnn1nja.guhca.Main.*;

public class Tools {

    @SuppressWarnings("deprecation")
    public static void initScoreboard(){
        //Init
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        //Teams
        for(Team t: board.getTeams()){
            if(t.getName().equalsIgnoreCase("guhcaOnline")){
                t.unregister();
            }else if(t.getName().equalsIgnoreCase("guhcaAFK")){
                t.unregister();
            }
        }
        Online = board.registerNewTeam("guhcaOnline");
        Online.setColor(ChatColor.RED);
        Online.setCanSeeFriendlyInvisibles(false);
        Afk = board.registerNewTeam("guhcaAFK");
        Afk.setColor(ChatColor.GRAY);
        Afk.setCanSeeFriendlyInvisibles(false);

        //Objectives
        for(Objective o: board.getObjectives()) {
            if (o.getName().equalsIgnoreCase("guhcaHealthBN")) {
                o.unregister();
            } else if (o.getName().equalsIgnoreCase("guhcaHealthPL")){
                o.unregister();
            }
        }
        HealthBN = board.registerNewObjective("guhcaHealthBN", "health");
        HealthBN.setDisplayName(ChatColor.DARK_RED + "♥");
        HealthBN.setDisplaySlot(DisplaySlot.BELOW_NAME);
        HealthPL = board.registerNewObjective("guhcaHealthPL", "health");
        HealthPL.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static void playersDied(){
        serverFreeze();
        playersDied = true;
        for(Player p: Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    public static void generateOfflinePlayerSet(){
        for(OfflinePlayer op: Bukkit.getOfflinePlayers()){
            OfflinePlayers.add(op.getName().toLowerCase());
        }
    }

    public static void serverFreeze(){
        if(!playersDied) {
            Bukkit.getServerTickManager().setFrozen(true);
            log.info("[Guhca] Server Frozen.");
        }
    }

    public static void serverResume(){
        if(!playersDied) {
            Bukkit.getServerTickManager().setFrozen(false);
            log.info("[Guhca] Server Resumed.");
        }
    }

    public static String stripMCCodes(String s){
        String bsChar = "\u00A7";
        StringBuilder output = new StringBuilder(s);
        while (output.toString().contains(bsChar)){
            int i = output.indexOf(bsChar);
            output.deleteCharAt(i);
            output.deleteCharAt(i);
        }

        return output.toString();
    }

}
