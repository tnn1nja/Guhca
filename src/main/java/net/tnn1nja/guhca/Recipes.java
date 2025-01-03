package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import static net.tnn1nja.guhca.Main.me;

public class Recipes {

    public static void registerRecipes(){
        //Ruby Shard Furnace
        Bukkit.addRecipe(new FurnaceRecipe(
                new NamespacedKey(me, "furnace/ruby_shard"),
                getRubyShard(),
                Material.HEAVY_CORE,
                2F,
                200));

        //Ruby Shard Blasting
        Bukkit.addRecipe(new BlastingRecipe(
                new NamespacedKey(me, "blasting/ruby_shard"),
                getRubyShard(),
                Material.HEAVY_CORE,
                2F,
                100));

        //Ruby Heart Crafting
        ShapedRecipe rubyHeartRecipe =
                new ShapedRecipe(new NamespacedKey(me, "crafting/ruby_heart"),
                getRubyHeart());
        rubyHeartRecipe.shape("R R", " R ", "   ");
        rubyHeartRecipe.setIngredient('R', Material.STRUCTURE_BLOCK);
        Bukkit.addRecipe(rubyHeartRecipe);
    }


    public static ItemStack getRubyHeart(){
        ItemStack rubyHeart = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = rubyHeart.getItemMeta();
        meta.setItemName("Ruby Heart");
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.setItemModel(new NamespacedKey("guhca", "ruby_heart"));
        rubyHeart.setItemMeta(meta);
        return rubyHeart;
    }

    public static ItemStack getRubyShard(){
        ItemStack rubyShard = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta meta = rubyShard.getItemMeta();
        meta.setItemName("Ruby Shard");
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemModel(new NamespacedKey("guhca", "ruby_shard"));
        rubyShard.setItemMeta(meta);
        return rubyShard;
    }
}
