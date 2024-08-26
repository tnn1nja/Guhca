package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    public static void saveData() {
        DataStore fileData = new DataStore(playerDeaths, survived);

        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream
                    (new GZIPOutputStream(Files.newOutputStream(Paths.get(dataFile))));
            out.writeObject(fileData);
            out.close();
            log.info("[Gucha] Data file saved.");
        } catch (IOException e) {
            log.info("[Guhca] Data file not found, creating one...");
        }
    }

    public static void loadData() {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream
                    (new GZIPInputStream(Files.newInputStream(Paths.get(dataFile))));
            DataStore fileData = (DataStore) in.readObject();
            in.close();
            playerDeaths = fileData.filePlayerDeaths;
            survived = fileData.fileSurvived;
            log.info("[Gucha] Data file loaded.");
        } catch (ClassNotFoundException | IOException e) {
            log.info("[Guhca] Data file not found, creating one...");
        }
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
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
        }
    }

    public static long getSecsSurvived(){
        long pt = 0;
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            int ticks = op.getStatistic(Statistic.TOTAL_WORLD_TIME);
            pt += ticks/20;
        }
        return pt;
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

    public static String stripColor(String s){
        String bsChar = "\u00A7";
        StringBuilder output = new StringBuilder(s);
        while (output.toString().contains(bsChar)){
            int i = output.indexOf(bsChar);
            output.deleteCharAt(i);
            output.deleteCharAt(i);
        }

        return output.toString();
    }


    public static class DataStore implements Serializable {
        public HashMap<String, Integer> filePlayerDeaths;
        public long fileSurvived;

        public DataStore(HashMap<String, Integer> hm, long l){
            filePlayerDeaths = hm;
            fileSurvived = l;
        }
    }

}
