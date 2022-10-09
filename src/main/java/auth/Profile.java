package auth;

public class Profile {
    private final String token;
    private final int groupId;

    Profile(String token, int groupId) {
        this.token = token;
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getToken() {
        return token;
    }
}
