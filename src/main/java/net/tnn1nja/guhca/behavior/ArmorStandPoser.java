package net.tnn1nja.guhca.behavior;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

public class ArmorStandPoser extends BehaviorCore {

    public static NamespacedKey armorStandDataKey = new NamespacedKey("guhca", "pose_id");
    public static ArmorStandPose[] armorStandPoses = createArmorStandPoseArray();
    public static ArmorStandPose[] createArmorStandPoseArray(){
        return new ArmorStandPose[]{
                new ArmorStandPose(
                        new double[]{0, 0, 0}, new double[]{0, 0, 0},
                        new double[]{6.11, 0, 6.11}, new double[]{6.02, 0, 0.175},
                        new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
                ),
                new ArmorStandPose(
                        new double[]{0, 0, 0}, new double[]{0, 0, 0},
                        new double[]{0, 0, 0}, new double[]{0, 0, 0},
                        new double[]{0, 0, 0}, new double[]{0, 0, 0}
                ),
                new ArmorStandPose(
                        new double[]{0.262, 0, 0}, new double[]{0, 0, 0.0349},
                        new double[]{5.76, 0.262, 0.262}, new double[]{5.24, 5.93, 6.11},
                        new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
                ),
                new ArmorStandPose(
                        new double[]{6.2, 0, 0}, new double[]{0, 0, 0.0349},
                        new double[]{0.175, 0, 6.2}, new double[]{5.24, 0.349, 6.11},
                        new double[]{6.23, 6.23, 6.23}, new double[]{0.0524, 0.0524, 0.0524}
                ),
                new ArmorStandPose(
                        new double[]{6.02, 0, 0}, new double[]{0, 0, 6.25},
                        new double[]{0.349, 0, 6.11}, new double[]{4.36, 0.873, 0},
                        new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
                ),
                new ArmorStandPose(
                        new double[]{6.02, 0, 0}, new double[]{0, 0, 0},
                        new double[]{4.36, 0.611, 0}, new double[]{4.36, 5.67, 0},
                        new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
                ),
                new ArmorStandPose(
                        new double[]{6.02, 0, 0}, new double[]{0, 0, 0},
                        new double[]{4.36, 5.67, 0}, new double[]{4.36, 0.611, 0},
                        new double[]{0.0873, 6.23, 6.23}, new double[]{6.2, 0.0524, 0.0524}
                ),
                new ArmorStandPose(
                        new double[]{0, 0, 0}, new double[]{0, 0, 0},
                        new double[]{0.175, 0, 6.2}, new double[]{5.06, 5.59, 0},
                        new double[]{6.27, 0, 6.27}, new double[]{0.0175, 0, 0.0175}
                ),
                new ArmorStandPose(
                        new double[]{0.279, 0.349, 0}, new double[]{0, 0, 0},
                        new double[]{0.07, 0.14, 4.14}, new double[]{4.29, 0, 1.55},
                        new double[]{6.04, 5.97, 6.00}, new double[]{0.14, 0.349, 0.07}
                ),
                new ArmorStandPose(
                        new double[]{6.11, 0, 6.2}, new double[]{0, 0, 0},
                        new double[]{4.45, 0, 0}, new double[]{4.54, 0, 0},
                        new double[]{0.122, 0, 0}, new double[]{5.48, 0, 0}
                ),
                new ArmorStandPose(
                        new double[]{6.2, 0.314, 0}, new double[]{0, 0.384, 0},
                        new double[]{0.14, 0, 4.29}, new double[]{0, 1.47, 1.94},
                        new double[]{4.35, 0.96, 0}, new double[]{0, 0.401, 6.06}
                ),
                new ArmorStandPose(
                        new double[]{6.11, 5.93, 0}, new double[]{0, 5.96, 0},
                        new double[]{0, 0, 4.33}, new double[]{0.14, 1.57, 1.94},
                        new double[]{0, 0, 0.227}, new double[]{4.2, 5.55, 0}
                ),
                new ArmorStandPose(
                        new double[]{6.22, 1.17, 0}, new double[]{0, 0.14, 0},
                        new double[]{0.279, 0.559, 6.14}, new double[]{4.56, 1.1, 0},
                        new double[]{0, 4.97, 6.14}, new double[]{0.07, 1.1, 0.14}
                )
        };
    };

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e){
        //Toggle Armor Stand Pose
        if(e.getRightClicked() instanceof ArmorStand as){
            if(e.getPlayer().isSneaking()){
                setPose(as, (getPose(as)+1)%13);
                e.getPlayer().swingMainHand();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(EntitySpawnEvent e){
        if (e.getEntity() instanceof ArmorStand as){
            setPose(as, 0);
            as.setArms(true);
        }
    }

    public static int getPose(ArmorStand as){
        PersistentDataContainer pdh = as.getPersistentDataContainer();
        if(pdh.has(armorStandDataKey)) {
            return pdh.get(armorStandDataKey, PersistentDataType.INTEGER);
        }else{
            return 0;
        }
    }

    public static void setPose(ArmorStand as, int id){
        as.getPersistentDataContainer().set(armorStandDataKey, PersistentDataType.INTEGER, id);

        ArmorStandPose asp = armorStandPoses[id];
        as.setHeadPose(asp.HEAD_POSE);
        as.setBodyPose(asp.BODY_POSE);
        as.setLeftArmPose(asp.LEFT_ARM_POSE);
        as.setRightArmPose(asp.RIGHT_ARM_POSE);
        as.setLeftLegPose(asp.LEFT_LEG_POSE);
        as.setRightLegPose(asp.RIGHT_LEG_POSE);
    }

    public static class ArmorStandPose{

        public EulerAngle HEAD_POSE;
        public EulerAngle BODY_POSE;
        public EulerAngle LEFT_ARM_POSE;
        public EulerAngle RIGHT_ARM_POSE;
        public EulerAngle LEFT_LEG_POSE;
        public EulerAngle RIGHT_LEG_POSE;

        public ArmorStandPose(double[] headPose, double[] bodyPose,
                              double[] leftArmPose, double[] rightArmPose,
                              double[] leftLegPose, double[] rightLegPose) {
            try {
                HEAD_POSE = new EulerAngle(headPose[0], headPose[1], headPose[2]);
                BODY_POSE = new EulerAngle(bodyPose[0], bodyPose[1], bodyPose[2]);
                LEFT_ARM_POSE = new EulerAngle(leftArmPose[0], leftArmPose[1], leftArmPose[2]);
                RIGHT_ARM_POSE = new EulerAngle(rightArmPose[0], rightArmPose[1], rightArmPose[2]);
                LEFT_LEG_POSE = new EulerAngle(leftLegPose[0], leftLegPose[1], leftLegPose[2]);
                RIGHT_LEG_POSE = new EulerAngle(rightLegPose[0], rightLegPose[1], rightLegPose[2]);
            }catch(IndexOutOfBoundsException e){
                throw new IllegalArgumentException("All double arrays must be three elements long.");
            }
        }

    }
}
