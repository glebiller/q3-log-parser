package fr.kissy.q3logparser.dto;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Streak {
    private Integer frag = 0;
    private Integer currentFrag = 0;
    private Integer death = 0;
    private Integer currentDeath = 0;

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

    public Integer getCurrentFrag() {
        return currentFrag;
    }

    public void setCurrentFrag(Integer currentFrag) {
        this.currentFrag = currentFrag;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public Integer getCurrentDeath() {
        return currentDeath;
    }

    public void setCurrentDeath(Integer currentDeath) {
        this.currentDeath = currentDeath;
    }
}
