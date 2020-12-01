package org.gjgr.pig.chivalrous.mongo;

import com.google.gson.JsonObject;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;

/**
 * @author gongwendong
 * @time 12-01-2020  星期二
 * @description: miparent:
 * @target:
 * @more:
 */
public class CredentialSettings {
    private final String FIELD_USERNAME = "username";
    private final String FIELD_PASSWORD = "password";
    private final String FIELD_AUTH_DB = "authDB";

    private boolean auth;

    private String username;
    private String authDatabase;
    private String password;

    public void init(String string) {
        JsonObject jsonObject = JsonCommand.jsonObject(string);
        if (null == settingNode || settingNode.isMissingNode()) {
            auth = false;
        }
        this.username = settingNode.path(FIELD_USERNAME).asText();
        this.password = settingNode.path(FIELD_PASSWORD).asText();
        this.authDatabase = settingNode.path(FIELD_AUTH_DB).asText();

        auth = StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(authDatabase);
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
