package auth;

import java.io.Serializable;

public class AuthData implements Serializable {
    private final String token;
    private final int groupId;

    AuthData(String token, int groupId) {
        this.token = token;
        this.groupId = groupId;
    }

    public String getToken() {
        return token;
    }


    public int getGroupId() {
        return groupId;
    }

}
