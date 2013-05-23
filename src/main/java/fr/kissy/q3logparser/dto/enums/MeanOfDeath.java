package fr.kissy.q3logparser.dto.enums;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public enum MeanOfDeath {
    MOD_UNKNOWN(""),
    MOD_SHOTGUN("Shotgun"),
    MOD_GAUNTLET("Gauntlet"),
    MOD_MACHINEGUN("MachineGun"),
    MOD_GRENADE("Grenade"),
    MOD_GRENADE_SPLASH("Grenade splash"),
    MOD_ROCKET("Rocket"),
    MOD_ROCKET_SPLASH("Rocket splash"),
    MOD_PLASMA("Plasma"),
    MOD_PLASMA_SPLASH("Plasma splash"),
    MOD_RAILGUN("Railgun"),
    MOD_LIGHTNING("Lightning"),
    MOD_BFG("Bfg"),
    MOD_BFG_SPLASH("Bfg splash"),
    MOD_WATER("Water"),
    MOD_SLIME("Slime"),
    MOD_LAVA("Lava"),
    MOD_CRUSH("Crush"),
    MOD_TELEFRAG(""),
    MOD_FALLING("Falling"),
    MOD_SUICIDE("Suicide"),
    MOD_TARGET_LASER(""),
    MOD_TRIGGER_HURT("Self damage"),
    MOD_NAIL(""),
    MOD_CHAINGUN("ChainGun"),
    MOD_PROXIMITY_MINE("Proximity Mine"),
    MOD_KAMIKAZE(""),
    MOD_JUICED(""),
    MOD_GRAPPLE("");

    private final String name;

    private MeanOfDeath(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
