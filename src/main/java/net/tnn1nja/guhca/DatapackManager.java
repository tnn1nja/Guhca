package net.tnn1nja.guhca;

import org.bukkit.*;

import java.io.*;

import static net.tnn1nja.guhca.Guhca.guhca;

public class DatapackManager {

    static String[][] index = {
            {"pack.mcmeta",                     ""},
            {"reward_ominous_unique.json",      "data/minecraft/loot_table/chests/trial_chambers/"}
    };

    public static void reloadDatapack(){
        String datapackDir = Bukkit.getWorlds().get(0).getName() + "/datapacks/guhca/";

        File datapackFile = new File(datapackDir);
        if(datapackFile.exists()){
            guhca.log("Datapack previously installed, updating...");
            recursiveDelete(datapackFile);
        }

        try {
            for (String[] pair : index) {
                new File(datapackDir + pair[1]).mkdirs();
                InputStream is = DatapackManager.class.getClassLoader().getResourceAsStream("datapack/" + pair[0]);
                FileOutputStream fos = new FileOutputStream(datapackDir + pair[1] + pair[0]);
                is.transferTo(fos);
                is.close();
                fos.close();
            }
            guhca.log("Datapack installed, reloading data...");
            Bukkit.getServer().reloadData();
        } catch (Exception e) {
            guhca.getLogger().severe("Datapack failed to install");
            e.printStackTrace();
        }
    }

    public static void recursiveDelete(File f){
        try{
            if (f.isDirectory()) {
                File[] list = f.listFiles();
                for (File target : list) {
                    recursiveDelete(target);
                }
            }
            f.delete();
        }catch(Exception e){
            guhca.log("Datapack failed to uninstall");
            e.printStackTrace();
        }
    }

}
