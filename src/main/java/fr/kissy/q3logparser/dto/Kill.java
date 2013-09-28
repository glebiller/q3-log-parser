package fr.kissy.q3logparser.dto;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.base.Objects;
import fr.kissy.q3logparser.enums.EnumMeanOfDeath;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Kill implements KryoSerializable {
    private Player player;
    private Integer playerId;
    private Player target;
    private Integer targetId;
    private Long time;
    private EnumMeanOfDeath meanOfDeath;

    public Kill() {
    }

    public Kill(Player player, Player target, Long time, EnumMeanOfDeath meanOfDeath) {
        this.player = player;
        this.target = target;
        this.time = time;
        this.meanOfDeath = meanOfDeath;
    }

    public boolean isSuicide() {
        return Objects.equal(player, target);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public EnumMeanOfDeath getMeanOfDeath() {
        return meanOfDeath;
    }

    public void setMeanOfDeath(EnumMeanOfDeath meanOfDeath) {
        this.meanOfDeath = meanOfDeath;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Kill){
            final Kill kill = (Kill) o;
            return Objects.equal(player, kill.player)
                    && Objects.equal(target, kill.target)
                    && Objects.equal(time, kill.time)
                    && Objects.equal(meanOfDeath, kill.meanOfDeath);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(player, target, time, meanOfDeath);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(player.getId());
        output.writeInt(target.getId());
        output.writeLong(time);
        kryo.writeObject(output, meanOfDeath);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        playerId = input.readInt();
        targetId = input.readInt();
        time = input.readLong();
        meanOfDeath = kryo.readObject(input, EnumMeanOfDeath.class);
    }
}
