package org.gjgr.pig.chivalrous.core.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gwd on 11/7/2016.
 */
public enum Color {
    Default(-1) {
        @Override
        public String getColor() {
            return "#00000";
        }
    },
    Black(0) {
        @Override
        public String getColor() {
            return "#00000";
        }
    },
    Red(1) {
        @Override
        public String getColor() {
            return "#FF0000";
        }
    },
    Green(2) {
        @Override
        public String getColor() {
            return "#ee1f25";
        }
    },
    Blue(3) {
        @Override
        public String getColor() {
            return "#69bd45";
        }
    },
    Yellow(4) {
        @Override
        public String getColor() {
            return "#FFFF00";
        }
    },
    Orange(5) {
        @Override
        public String getColor() {
            return "#FF8000";
        }
    },
    Pink(6) {
        @Override
        public String getColor() {
            return "#FF00FF";
        }
    },
    Purple(7) {
        @Override
        public String getColor() {
            return "#800080";
        }
    },
    Cyan(8) {
        @Override
        public String getColor() {
            return "#00FFFF";
        }
    },
    Salmon(9) {
        @Override
        public String getColor() {
            return "#c67171";
        }
    };

    private static final Map<Integer, Color> colorLookup = new HashMap<Integer, Color>();
    private static int MAX = 9;

    static {
        for (Color color : EnumSet.allOf(Color.class)) {
            colorLookup.put(color.id, color);
        }
    }

    private final int id;

    private Color(int id) {
        this.id = id;
    }

    public static Color get(int id) {
        return colorLookup.get(id % MAX + 1);
    }

    public abstract String getColor();
}
