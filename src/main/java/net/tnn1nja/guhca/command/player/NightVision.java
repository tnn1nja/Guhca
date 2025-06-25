package net.tnn1nja.guhca.command.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class NightVision extends PlayerCommand {

    @Override
    protected boolean shouldExecute(Player p, String[] args) {
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))){
            p.sendMessage(text("Only a spectator to run this command", RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onExecute(Player p, String[] args) {
        if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }else{
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                    PotionEffect.INFINITE_DURATION, 0, false, false));
        }
    }

    @Override
    protected List<String> getSuggestions(Player p, String[] args) {
        return none;
    }

}
