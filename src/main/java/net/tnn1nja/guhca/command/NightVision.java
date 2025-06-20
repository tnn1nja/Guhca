package net.tnn1nja.guhca.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NightVision extends CommandCore{
    @Override
    String getName() {
        return "nightvision";
    }

    @Override
    void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player p)){
            sender.sendMessage(Component.text("Only a player can run this command", NamedTextColor.RED));
            return;
        }

        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            sender.sendMessage(Component.text("Only a spectator to run this command", NamedTextColor.RED));
            return;
        }

        if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }else{
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                    PotionEffect.INFINITE_DURATION, 0, false, false));
        }
    }

    @Override
    List<String> getSuggestion(CommandSender sender, String[] args) {
        return empty;
    }
}
