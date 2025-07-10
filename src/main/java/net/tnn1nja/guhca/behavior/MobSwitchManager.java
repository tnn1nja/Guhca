package net.tnn1nja.guhca.behavior;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashSet;
import java.util.UUID;

import static net.tnn1nja.guhca.Guhca.guhca;

public class MobSwitchManager extends BehaviorCore {

    public static HashSet<UUID> mobSwitchedWorlds = new HashSet<UUID>();

    @Override
    public void onEnable() {
        registerRepeatingTask(this::eachFifteenSeconds, 300);
    }

    public void eachFifteenSeconds(){
        for(World w: Bukkit.getWorlds()) {
            int validZombieVillagers = 0;
            for (LivingEntity le : w.getLivingEntities()) {
                if (le.getType() == EntityType.ZOMBIE_VILLAGER) {
                    if (le.getRemoveWhenFarAway()) {
                        validZombieVillagers += 1;
                    }
                }
            }

            String dimension = switch (w.getEnvironment()) {
                case NORMAL -> "overworld";
                case NETHER -> "nether";
                case THE_END -> "end";
                default -> "custom dimension";
            };

            if (validZombieVillagers > (70 * Bukkit.getOnlinePlayers().size())) {
                if (mobSwitchedWorlds.add(w.getUID())) {
                    guhca.log("Mob switch enabled for the " + dimension);
                }
            } else {
                if (mobSwitchedWorlds.remove(w.getUID())) {
                    guhca.log("Mob Switch Disabled for the " + dimension);
                }
            }
        }
    }

    @EventHandler
    public void onMonsterSpawn(CreatureSpawnEvent e){
        if(mobSwitchedWorlds.contains(e.getLocation().getWorld().getUID()) &&
                e.getEntity().getSpawnCategory() == SpawnCategory.MONSTER &&
                ((e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL &&
                        e.getEntityType() != EntityType.WARDEN) ||
                        (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.PATROL &&
                                e.getEntityType() == EntityType.PHANTOM))
        ){
            e.setCancelled(true);
        }
    }

}
