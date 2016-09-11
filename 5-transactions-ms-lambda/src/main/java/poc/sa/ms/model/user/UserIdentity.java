package poc.sa.ms.model.user;

/*
* The user identity bean - this is used by the credentials provider to store a unique identity id (for examaple a Cognito id)
* and an OpenID token for the user. The OpenID token can be exchanged for temporary AWS credentials.
*/
public class UserIdentity {
   private String openIdToken;
   private String identityId;

   public String getOpenIdToken() {
       return openIdToken;
   }

   public void setOpenIdToken(String openIdToken) {
       this.openIdToken = openIdToken;
   }

   public String getIdentityId() {
       return identityId;
   }

   public void setIdentityId(String identityId) {
       this.identityId = identityId;
   }
}
