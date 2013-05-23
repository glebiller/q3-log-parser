package fr.kissy.q3logparser.dto;

import com.google.common.base.Functions;
import com.google.common.collect.*;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import sun.awt.windows.WPageDialog;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Player {
    private Team team = null;
    private String name = null;
    private Boolean hasFlag = false;

    private Integer score = 0;

    private Streak streak = new Streak();
    private Flag flag = new Flag();

    transient private List<Kill> frags = Lists.newArrayList();
    transient private List<Kill> deaths = Lists.newArrayList();
    transient private List<Kill> suicides = Lists.newArrayList();
    transient private Map<MeanOfDeath, Weapons> weapons = Maps.newTreeMap();

    public Player() {
        for (MeanOfDeath meanOfDeath : MeanOfDeath.values()) {
            weapons.put(meanOfDeath, new Weapons(meanOfDeath));
        }
    }

    public void update(String name, Integer teamNumber) {
        this.name = name;
        this.team = Team.values()[teamNumber];
    }

    public void addKill(Kill kill) {
        if (kill.getTarget().equals(this)) {
            streak.addDeath();
            deaths.add(kill);
            if (kill.isSuicide()) {
                suicides.add(kill);
            }
        } else if (kill.getPlayer().equals(this)) {
            streak.addFrag();
            frags.add(kill);

            // Stats
            weapons.get(kill.getMeanOfDeath()).addFrag();
        }

        // Flag update
        if (kill.getTarget().equals(this) && hasFlag) {
            this.hasFlag = false;
        }
    }

    public void captureFlag() {
        this.hasFlag = false;
        this.flag.addCaptured();
    }

    public void pickupFlag() {
        this.hasFlag = true;
        this.flag.addPickedUp();
    }

    public void returnFlag() {
        this.flag.addReturned();
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamCssClass() {
        return team.getCssClass();
    }

    public String getTeamName() {
        return team.getName();
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasFlag() {
        return hasFlag;
    }

    public void setHasFlag(Boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Streak getStreak() {
        return streak;
    }

    public void setStreak(Streak streak) {
        this.streak = streak;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public List<Kill> getFrags() {
        return frags;
    }

    public void setFrags(List<Kill> frags) {
        this.frags = frags;
    }

    public List<Kill> getDeaths() {
        return deaths;
    }

    public void setDeaths(List<Kill> deaths) {
        this.deaths = deaths;
    }

    public List<Kill> getSuicides() {
        return suicides;
    }

    public void setSuicides(List<Kill> suicides) {
        this.suicides = suicides;
    }

    public Map<MeanOfDeath, Weapons> getWeapons() {
        return weapons;
    }

    public List<Weapons> getSortedWeapons() {
        // Special, sort it before
        return Ordering.natural().immutableSortedCopy(weapons.values());
    }

    public void setWeapons(Map<MeanOfDeath, Weapons> weapons) {
        this.weapons = weapons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE, null, null, false, false).toString();
    }
}
