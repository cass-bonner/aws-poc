package poc.sa.ms.provider;

import poc.sa.ms.model.user.User;
import poc.sa.ms.model.user.UserCredentials;
import poc.sa.ms.model.user.UserIdentity;

/**
 * The CredentialsProvider interface used by the Login and Registration methods to retrieve temporary AWS credentials
 * for a user
 */
public interface CredentialsProvider {
    /**
     * Generates and returns a set of temporary AWS credentials for an end user
     *
     * @param user The end user object. The identity property in the User object needs to be populated with a valid
     *             identityId and openID Token
     * @return A populated UserCredentials object
     * @throws AuthorizationException
     */
    UserCredentials getUserCredentials(User user) throws RuntimeException;

    /**
     * Gets a unique identifier and a valid OpenID token for a user
     *
     * @param user The user that is logging in or registering
     * @return A populated user identity object with an OpenID token and identityId
     * @throws AuthorizationException
     */
    UserIdentity getUserIdentity(User user) throws RuntimeException;
}

