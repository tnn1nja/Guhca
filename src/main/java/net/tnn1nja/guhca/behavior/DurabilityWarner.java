package net.tnn1nja.guhca.behavior;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class DurabilityWarner extends BehaviorCore{

    @EventHandler
    public void onToolDurabilityDecrease(PlayerItemDamageEvent e){
        Player p = e.getPlayer();
        ItemStack is = e.getItem();
        int maxDurability = is.getType().getMaxDurability();
        int remainingDurability = maxDurability - ((Damageable) is.getItemMeta()).getDamage();
        float durability = remainingDurability / (float) maxDurability;
        if (durability < 0.1){
            p.sendActionBar(Component.text("Severe Warning: low durability", NamedTextColor.RED));
        }else if(durability < 0.2){
            p.sendActionBar(Component.text("Warning: low durability", NamedTextColor.GOLD));
        }
    }

}
