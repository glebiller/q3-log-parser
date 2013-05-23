package fr.kissy.q3logparser.dto.enums;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public enum GameType {
    FREE_FOR_ALL("FFA"),
    TOURNAMENT("TOUR"),
    SINGLE_PLAYER("SP"),
    TEAM_DEATHMATCH("TDM"),
    CAPTURE_THE_FLAG("CTF");

    private final String name;

    private GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
