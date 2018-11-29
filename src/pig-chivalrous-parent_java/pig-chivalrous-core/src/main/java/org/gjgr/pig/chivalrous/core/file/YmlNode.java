package org.gjgr.pig.chivalrous.core.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author gwd
 * @Time 09-28-2018  Friday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class YmlNode implements Serializable {
    private Object object;

    public Object getObject() {
        return object;
    }

    public YmlNode setObject(Object object) {
        this.object = object;
        return this;
    }

    public <T> T get() {
        return (T) getObject();
    }

    public String string() {
        if (object instanceof String) {
            return getObject() == null ? null : getObject().toString();
        } else {
            return null;
        }
    }

    public String value() {
        if (object == null) {
            return null;
        } else if (object instanceof List) {
            if (((List) (object)).size() > 0) {
                return ((List) object).stream().map(Object::toString).collect(Collectors.joining("\n")).toString();
            } else {
                return null;
            }
        } else if (object instanceof String) {
            if (object != null) {
                return (String) object;
            } else {
                return null;
            }
        } else {
            throw new ClassCastException();
        }
    }

    public Integer intValue() {
        if (object != null) {
            try {
                Integer anInt = Integer.parseInt(object.toString());
                return anInt;
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Long longValue() {
        if (object != null) {
            try {
                Long aLong = Long.parseLong(object.toString());
                return aLong;
            } catch (Exception e) {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public Float floatValue() {
        if (object != null) {
            try {
                Float aFloat = Float.parseFloat(object.toString());
                return aFloat;
            } catch (Exception e) {
                return 0F;
            }
        } else {
            return 0F;
        }
    }

    public Double doubleValue() {
        if (object != null) {
            try {
                Double aDouble = Double.parseDouble(object.toString());
                return aDouble;
            } catch (Exception e) {
                return 0d;
            }
        } else {
            return 0d;
        }
    }

    public <T> T getValue(T t) {
        Object oo = object();
        Class clazz = t.getClass();
        try {
            if (oo != null && clazz != null && clazz.equals(Integer.class.getClass())) {
                Integer a = Integer.parseInt((String) oo);
                return (T) a;
            } else if (oo != null && clazz != null && clazz.equals(Double.class.getClass())) {
                Double a = Double.parseDouble((String) oo);
                return (T) a;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Object object() {
        return object;
    }

    public Map map() {
        if (object == null) {
            return new HashMap();
        } else if (object instanceof Map) {
            return (Map) object;
        } else {
            throw new ClassCastException();
        }
    }

    public List list() {
        if (object == null) {
            return new ArrayList();
        } else if (object instanceof List) {
            return (List) object;
        } else {
            throw new ClassCastException();
        }
    }

    public YmlNode get(Object key) {
        if (object == null) {
            return this;
        } else if (object instanceof Map && key instanceof String) {
            if (((Map) object).containsKey(key)) {
                return new YmlNode().setObject(((Map) object).get(key));
            } else {
                return new YmlNode();
            }
        } else if (object instanceof List && key instanceof Integer) {
            if (((Integer) key >= 0) && (((List) object).size() < (Integer) key)) {
                return new YmlNode().setObject(((List) object).get((Integer) key));
            }
            return new YmlNode().setObject(object);
        } else {
            throw new ClassCastException();
        }
    }
}
