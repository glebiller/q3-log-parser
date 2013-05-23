package fr.kissy.q3logparser.dto;

import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Kill {
    private Player player;
    private Player target;
    private Integer time;
    private MeanOfDeath meanOfDeath;

    public Kill(Player player, Player target, Integer time, MeanOfDeath meanOfDeath) {
        this.player = player;
        this.target = target;
        this.time = time;
        this.meanOfDeath = meanOfDeath;
    }

    public boolean isSuicide() {
        return player.equals(target);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public MeanOfDeath getMeanOfDeath() {
        return meanOfDeath;
    }

    public void setMeanOfDeath(MeanOfDeath meanOfDeath) {
        this.meanOfDeath = meanOfDeath;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
