package net.tnn1nja.guhca;

import net.tnn1nja.guhca.behavior.BehaviorCore;
import net.tnn1nja.guhca.command.CommandCore;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Guhca extends JavaPlugin {

    public static Guhca guhca;
    //test:q

    @Override
    public void onEnable() {
        guhca = this;
        guhca.log("Guhca enabled");

        CommandCore.registerCommands();
        BehaviorCore.registerBehaviors();
        DatapackManager.reloadDatapack();

        for(World w: Bukkit.getWorlds()){
            w.setGameRule(GameRule.NATURAL_REGENERATION, false);
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
            w.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
            w.setGameRule(GameRule.DISABLE_PLAYER_MOVEMENT_CHECK, true);
            w.setDifficulty(Difficulty.HARD);
        }

    }

    @Override
    public void onDisable() {
        guhca.log("Guhca disabled");
    }

    public void log(String s){
        guhca.getLogger().info(s);
    }

}
