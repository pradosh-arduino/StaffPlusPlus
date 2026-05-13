package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.utils.Materials;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.StaffItemsConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.StaffModesLoader;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Map;

@IocBean
public class Options {

    @ConfigProperty("permissions:member")
    public String permissionMember;
    @ConfigProperty("server-name")
    public String serverName;
    @ConfigProperty("main-world")
    public String mainWorld;
    @ConfigProperty("timestamp-format")
    public String timestampFormat;

    public Map<String, GeneralModeConfiguration> modeConfigurations;
    public final ServerSyncConfiguration serverSyncConfiguration;
    public final StaffItemsConfiguration staffItemsConfiguration;

    /*
     * Permissions
     */

    private final StaffModesLoader staffModesLoader;

    public Options(StaffModesLoader staffModesLoader,
                   ServerSyncConfiguration serverSyncConfiguration,
                   StaffItemsConfiguration staffItemsConfiguration) {
        this.staffModesLoader = staffModesLoader;
        this.serverSyncConfiguration = serverSyncConfiguration;
        this.staffItemsConfiguration = staffItemsConfiguration;
        reload();
    }

    public void reload() {
        modeConfigurations = this.staffModesLoader.loadConfig();
    }


    public static String getMaterial(String current) {
        String material = sanitizeMaterial(current);
        if (material.equals("INK") && current.contains(":")) {
            return getLegacyInkMaterialName(getMaterialData(current));
        }

        switch (material) {
            case "HEAD":
                return Materials.valueOf("HEAD").getName();
            case "SPAWNER":
                return Materials.valueOf("SPAWNER").getName();
            case "ENDEREYE":
                return Materials.valueOf("ENDEREYE").getName();
            case "CLOCK":
                return Materials.valueOf("CLOCK").getName();
            case "LEAD":
                return Materials.valueOf("LEAD").getName();
            case "INK":
                return Materials.valueOf("INK").getName();
            default:
                return material;

        }

    }

    public static Material stringToMaterial(String string) {
        Material sound = Material.STONE;
        String materialName = getMaterial(string);
        Material matchedMaterial = Material.matchMaterial(materialName);

        if (matchedMaterial == null) {
            Bukkit.getLogger().severe("Invalid material type '" + string + "'!");
        } else {
            sound = matchedMaterial;
        }

        return sound;
    }

    public static String sanitizeMaterial(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }

    private static String getLegacyInkMaterialName(short data) {
        switch (data) {
            case 1:
                return "RED_DYE";
            case 2:
                return "GREEN_DYE";
            case 3:
                return "COCOA_BEANS";
            case 4:
                return "LAPIS_LAZULI";
            case 5:
                return "PURPLE_DYE";
            case 6:
                return "CYAN_DYE";
            case 7:
                return "LIGHT_GRAY_DYE";
            case 8:
                return "GRAY_DYE";
            case 9:
                return "PINK_DYE";
            case 10:
                return "LIME_DYE";
            case 11:
                return "YELLOW_DYE";
            case 12:
                return "LIGHT_BLUE_DYE";
            case 13:
                return "MAGENTA_DYE";
            case 14:
                return "ORANGE_DYE";
            case 15:
                return "BONE_MEAL";
            default:
                return Materials.valueOf("INK").getName();
        }
    }

    public static short getMaterialData(String string) {
        short data = 0;

        if (string.contains(":")) {
            String dataString = string.substring(string.lastIndexOf(':') + 1);

            if (JavaUtils.isInteger(dataString)) {
                data = (short) Integer.parseInt(dataString);
            } else
                Bukkit.getLogger().severe("Invalid material data '" + dataString + "' from '" + string + "'!");
        }

        return data;
    }
}