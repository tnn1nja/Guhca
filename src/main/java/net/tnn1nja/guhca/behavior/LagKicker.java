package net.tnn1nja.guhca.behavior;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LagKicker extends BehaviorCore {

    public static int pingKickThreshold = 5000;

    @Override
    public void onEnable() {
        registerRepeatingTask(this::eachHalfSecond, 10);
    }

    public void eachHalfSecond(){
        for(Player p: Bukkit.getOnlinePlayers()){
            if(p.getPing() > pingKickThreshold){
                Component.text(p.getName() + " lagged out", NamedTextColor.GOLD);
                p.kick(Component.text("Your ping exceeded " + pingKickThreshold));
            }
        }
    }

}
