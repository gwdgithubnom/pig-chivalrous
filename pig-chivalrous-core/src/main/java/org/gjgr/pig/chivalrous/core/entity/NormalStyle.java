package org.gjgr.pig.chivalrous.core.entity;

import java.io.Serializable;

/**
 * Created by gwd on 11/7/2016.
 */
public class NormalStyle implements Serializable,Cloneable {
    private String color = "black";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
