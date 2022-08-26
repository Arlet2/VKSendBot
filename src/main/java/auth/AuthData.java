package auth;

public class AuthData {
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
