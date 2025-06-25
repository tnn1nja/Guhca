package net.tnn1nja.guhca.command;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class NightVision extends CommandCore {

    @Override
    protected boolean shouldExecute(CommandSender s, String[] args) {
        if(!(s instanceof Player p)){
            s.sendMessage(text("Only a player can run this command", RED));
            return false;
        }
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            s.sendMessage(text("Only a spectator to run this command", RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(CommandSender s, String[] args) {
        Player p = (Player) s;
        if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }else{
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                    PotionEffect.INFINITE_DURATION, 0, false, false));
        }
    }

    @Override
    protected List<String> getSuggestions(CommandSender s, String[] args) {
        return none;
    }

}
