package net.tnn1nja.guhca.behavior;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.tnn1nja.guhca.Guhca.guhca;

public class BehaviorCore implements Listener {

    //Static mechanic registry
    public static void registerBehaviors(){
        BehaviorCore[] registry = new BehaviorCore[]{
                new BehaviorCore(),
                new AntiEndermanGreifer(),
                new AnimalAgeToggler(),
                new ArmorStandPoser(),
                new CampfireBooster(),
                new DurabilityWarner(),
                new ElytraCanceller(),
                new ExperienceDelayRemover(),
                new HealthDisplayer(),
                new ItemFrameToggler(),
                new LagKicker(),
                new MobSwitchManager(),
                new RaidBellExtender(),
                new RecipeGranter(),
                new RegenerationSoupFixer()
        };
        for(BehaviorCore behavior: registry){
            guhca.getServer().getPluginManager().registerEvents(behavior, guhca);
            behavior.onEnable();
        }
        guhca.log("Mechanics registered (" + (registry.length-1) + ")");
    }

    public void onEnable(){};

    public void registerRepeatingTask(Runnable task, int period){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(guhca, task, 0, period);
    }

    @EventHandler(priority = EventPriority.HIGHEST) //ensure its run last
    public void onQuit(PlayerQuitEvent e){
        if(e.getReason() == PlayerQuitEvent.QuitReason.KICKED){
            e.quitMessage(null);
        }
    }

}
