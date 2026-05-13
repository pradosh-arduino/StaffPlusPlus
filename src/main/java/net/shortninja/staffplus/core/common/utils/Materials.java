package net.shortninja.staffplus.core.common.utils;


import org.bukkit.Material;

public enum Materials {
    SPAWNER("MOB_SPAWNER", "SPAWNER"),
    HEAD("SKULL_ITEM", "PLAYER_HEAD"),
    ENDEREYE("EYE_OF_ENDER", "ENDER_EYE"),
    CLOCK("WATCH", "CLOCK"),
    LEAD("LEASH", "LEAD"),
    INK("INK_SACK", "INK_SAC");

    private final String oldName, newName;

    Materials(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getName() {
        if (Material.matchMaterial(newName) != null) {
            return newName;
        }
        return oldName;
    }
}
