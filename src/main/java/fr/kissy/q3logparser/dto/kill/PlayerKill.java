package fr.kissy.q3logparser.dto.kill;

import com.google.common.base.Objects;
import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class PlayerKill implements Comparable<PlayerKill> {
    private Player player;
    private Integer frags = 0;

    public PlayerKill(Player player) {
        this.player = player;
    }

    public void addFrag() {
        ++this.frags;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getFrags() {
        return frags;
    }

    public void setFrags(Integer frags) {
        this.frags = frags;
    }

    @Override
    public int compareTo(PlayerKill weapons) {
        return weapons.getFrags().compareTo(frags);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PlayerKill){
            final PlayerKill playerKill = (PlayerKill) o;
            return Objects.equal(player, playerKill.player)
                    && Objects.equal(frags, playerKill.frags);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(player, frags);
    }
}
