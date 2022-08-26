public class AuthData {
    private final String token;
    private final String secretKey;
    private final String serviceKey;
    private final int appId;
    private final int groupId;

    AuthData(String token, int appId, String secretKey, String serviceKey, int groupId) {
        this.token = token;
        this.appId = appId;
        this.secretKey = secretKey;
        this.serviceKey = serviceKey;
        this.groupId = groupId;
    }

    public String getToken() {
        return token;
    }


    public int getGroupId() {
        return groupId;
    }

}
