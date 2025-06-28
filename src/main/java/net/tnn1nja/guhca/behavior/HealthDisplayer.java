package net.tnn1nja.guhca.behavior;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class HealthDisplayer extends BehaviorCore {

    public static Objective HealthName;
    public static Objective HealthList;

    @Override
    public void onEnable() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        for(Objective o: board.getObjectives()) {
            if (o.getName().equalsIgnoreCase("guhca.health_below_name")) {
                HealthName = o;
            } else if (o.getName().equalsIgnoreCase("guhca.health_player_list")){
                HealthList = o;
            }
        }

        if(HealthName == null){
            HealthName = board.registerNewObjective("guhca.health_below_name", Criteria.HEALTH,
                    Component.text("♥", NamedTextColor.DARK_RED));
        }
        if (HealthList == null){
            HealthList = board.registerNewObjective("guhca.health_player_list", Criteria.HEALTH,
                    Component.text("HealthList"));
        }

        //In case they are reset by operator
        HealthName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        HealthList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }
}
