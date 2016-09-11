package poc.sa.ms.model.action;

import poc.sa.ms.model.user.UserCredentials;

public class LoginUserResponse {
    private String identityId;
    private String token;
    private UserCredentials credentials;


    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }
}

