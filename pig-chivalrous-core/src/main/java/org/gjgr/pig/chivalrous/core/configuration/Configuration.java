package org.gjgr.pig.chivalrous.core.configuration;

import java.io.Serializable;
import java.util.Optional;

public interface Configuration extends Serializable {
    //    Configuration sparkConf();
    Optional<String> getOptionalString(String key, String defaultValue);

    Optional<String> getOptionalString(String key);

    Optional<Object> getOptionalObject(String key, Object defaultValue);

    Optional<Object> getOptionalObject(String key);
}
