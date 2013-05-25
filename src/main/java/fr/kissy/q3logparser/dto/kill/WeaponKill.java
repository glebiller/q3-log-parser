package fr.kissy.q3logparser.dto.kill;

import com.google.common.base.Objects;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class WeaponKill implements Comparable<WeaponKill> {
    private MeanOfDeath meanOfDeath;
    private Integer frags = 0;

    public WeaponKill(MeanOfDeath meanOfDeath) {
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
    public int compareTo(WeaponKill weapons) {
        return weapons.getFrags().compareTo(frags);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof WeaponKill){
            final WeaponKill weaponKill = (WeaponKill) o;
            return Objects.equal(meanOfDeath, weaponKill.meanOfDeath)
                    && Objects.equal(frags, weaponKill.frags);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(meanOfDeath, frags);
    }
}
