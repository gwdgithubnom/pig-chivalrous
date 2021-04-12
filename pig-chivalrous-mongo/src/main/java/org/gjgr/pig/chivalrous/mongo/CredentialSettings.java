package org.gjgr.pig.chivalrous.mongo;

import com.google.gson.JsonObject;
import java.io.Serializable;
import org.gjgr.pig.chivalrous.core.json.GsonObject;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * @author gongwendong
 * @time 12-01-2020  星期二
 * @description: miparent:
 * @target:
 * @more:
 */
public class CredentialSettings implements Serializable {
    protected static final long serialVersionUID = 1024L;

    private final String FIELD_USERNAME = "username";
    private final String FIELD_PASSWORD = "password";
    private final String FIELD_AUTH_DB = "authDB";
    private boolean auth;
    private String username;
    private String authDatabase;
    private String password;

    public void init(String string) {
        GsonObject settingNode = JsonCommand.gsonObject(string);
        if (null == settingNode || settingNode.isJsonNull()) {
            auth = false;
        }
        this.username = settingNode.getAsString(FIELD_USERNAME);
        this.password = settingNode.getAsString(FIELD_PASSWORD);
        this.authDatabase = settingNode.getAsString(FIELD_AUTH_DB);

        auth = StringCommand.isNotBlank(username) && StringCommand.isNotBlank(password) && StringCommand.isNotBlank(authDatabase);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthDatabase() {
        return authDatabase;
    }

    public void setAuthDatabase(String authDatabase) {
        this.authDatabase = authDatabase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuth() {
        return auth;
    }

    @Override
    public String toString() {
        return "CredentialSettings{" +
            "username='" + username + '\'' +
            ", authDatabase='" + authDatabase + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
