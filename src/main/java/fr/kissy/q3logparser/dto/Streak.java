package fr.kissy.q3logparser.dto;

import com.google.common.base.Objects;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Streak {
    private Integer frag = 0;
    private Integer death = 0;

    transient private Integer currentFrag = 0;
    transient private Integer currentDeath = 0;

    public void addFrag() {
        ++this.currentFrag;
        this.frag = Math.max(this.frag, this.currentFrag);
        this.currentDeath = 0;
    }

    public void addDeath() {
        ++this.currentDeath;
        this.death = Math.max(this.death, this.currentDeath);
        this.currentFrag = 0;
    }

    public Integer getFrag() {
        return frag;
    }

    public void setFrag(Integer frag) {
        this.frag = frag;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public Integer getCurrentFrag() {
        return currentFrag;
    }

    public void setCurrentFrag(Integer currentFrag) {
        this.currentFrag = currentFrag;
    }

    public Integer getCurrentDeath() {
        return currentDeath;
    }

    public void setCurrentDeath(Integer currentDeath) {
        this.currentDeath = currentDeath;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Streak){
            final Streak streak = (Streak) o;
            return Objects.equal(frag, streak.frag)
                    && Objects.equal(death, streak.death);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(frag, death);
    }
}
