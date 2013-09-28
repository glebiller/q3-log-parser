package fr.kissy.q3logparser.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public enum EnumTeam {
    TEAM_FREE("", ""),
    TEAM_RED("R", "danger"),
    TEAM_BLUE("B", "primary"),
    TEAM_SPECTATOR("", "");

    private final String name;
    private final String cssClass;

    private EnumTeam(String name, String cssClass) {
        this.name = name;
        this.cssClass = cssClass;
    }

    public String getName() {
        return name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public EnumTeam getOpposite() {
        switch (this) {
            case TEAM_BLUE:
                return TEAM_RED;
            case TEAM_RED:
                return TEAM_BLUE;
            default:
                return TEAM_FREE;
        }
    }

    public static EnumTeam fromFlagColor(String flagColor) {
        for (EnumTeam team : EnumTeam.values()) {
            if (StringUtils.containsIgnoreCase(team.name(), flagColor)) {
                return team;
            }
        }

        return null;
    }
}
