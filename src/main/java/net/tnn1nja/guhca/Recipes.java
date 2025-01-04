package net.tnn1nja.guhca;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import static net.tnn1nja.guhca.Main.me;

public class Recipes {

    public static void registerRecipes(){
        //Crystal Shard Furnace
        Bukkit.addRecipe(new FurnaceRecipe(
                new NamespacedKey(me, "furnace/crystal_shard"),
                getCrystalShard(),
                Material.HEAVY_CORE,
                2F,
                200));

        //Crystal Shard Blasting
        Bukkit.addRecipe(new BlastingRecipe(
                new NamespacedKey(me, "blasting/crystal_shard"),
                getCrystalShard(),
                Material.HEAVY_CORE,
                2F,
                100));

        //Crystal Heart Crafting
        ShapedRecipe crystalHeartRecipe =
                new ShapedRecipe(new NamespacedKey(me, "crafting/crystal_heart"),
                getCrystalHeart());
        crystalHeartRecipe.shape("R R", " R ", "   ");
        crystalHeartRecipe.setIngredient('R', Material.STRUCTURE_BLOCK);
        Bukkit.addRecipe(crystalHeartRecipe);
    }


    public static ItemStack getCrystalHeart(){
        ItemStack crystalHeart = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = crystalHeart.getItemMeta();
        meta.setItemName("Crystal Heart");
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.setItemModel(new NamespacedKey("guhca", "crystal_heart"));
        crystalHeart.setItemMeta(meta);
        return crystalHeart;
    }

    public static ItemStack getCrystalShard(){
        ItemStack crystalShard = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta meta = crystalShard.getItemMeta();
        meta.setItemName("Crystal Shard");
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemModel(new NamespacedKey("guhca", "crystal_shard"));
        crystalShard.setItemMeta(meta);
        return crystalShard;
    }
}
