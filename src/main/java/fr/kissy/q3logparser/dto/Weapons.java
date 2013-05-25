package fr.kissy.q3logparser.dto;

import com.google.common.base.Objects;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Weapons implements Comparable<Weapons> {
    private MeanOfDeath meanOfDeath;
    private Integer frags = 0;

    public Weapons(MeanOfDeath meanOfDeath) {
        this.meanOfDeath = meanOfDeath;
    }

    public void addFrag() {
        ++this.frags;
    }

    public MeanOfDeath getMeanOfDeath() {
        return meanOfDeath;
    }

    public String getMeanOfDeathName() {
        return meanOfDeath.getName();
    }

    public void setMeanOfDeath(MeanOfDeath meanOfDeath) {
        this.meanOfDeath = meanOfDeath;
    }

    public Integer getFrags() {
        return frags;
    }

    public void setFrags(Integer frags) {
        this.frags = frags;
    }

    @Override
    public int compareTo(Weapons weapons) {
        return weapons.getFrags().compareTo(frags);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Weapons){
            final Weapons weapons = (Weapons) o;
            return Objects.equal(meanOfDeath, weapons.meanOfDeath)
                    && Objects.equal(frags, weapons.frags);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(meanOfDeath, frags);
    }
}
