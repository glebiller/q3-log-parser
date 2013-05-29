package fr.kissy.q3logparser.dto.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public enum Team {
    TEAM_FREE("", ""),
    TEAM_RED("R", "important"),
    TEAM_BLUE("B", "info"),
    TEAM_SPECTATOR("", "");

    private final String name;
    private final String cssClass;

    private Team(String name, String cssClass) {
        this.name = name;
        this.cssClass = cssClass;
    }

    public String getName() {
        return name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public Team getOpposite() {
        switch (this) {
            case TEAM_BLUE:
                return TEAM_RED;
            case TEAM_RED:
                return TEAM_BLUE;
            default:
                return TEAM_FREE;
        }
    }

    public static Team fromFlagColor(String flagColor) {
        for (Team team : Team.values()) {
            if (StringUtils.containsIgnoreCase(team.name(), flagColor)) {
                return team;
            }
        }

        return null;
    }
}
