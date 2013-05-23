package fr.kissy.q3logparser.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Flag {
    private Integer captured = 0;
    private Integer pickedUp = 0;
    private Integer returned = 0;

    public void addCaptured() {
        ++this.captured;
    }

    public void addPickedUp() {
        ++this.pickedUp;
    }

    public void addReturned() {
        ++this.returned;
    }

    public Integer getCaptured() {
        return captured;
    }

    public void setCaptured(Integer captured) {
        this.captured = captured;
    }

    public Integer getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(Integer pickedUp) {
        this.pickedUp = pickedUp;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
