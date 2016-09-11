package poc.sa.ms.model.user;

import java.nio.ByteBuffer;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import poc.sa.ms.daoconfig.DdbConfiguration;

@DynamoDBTable(tableName = DdbConfiguration.USERS_TABLE_NAME)
public class User {
    private String username;
    private ByteBuffer password;
    private ByteBuffer salt;
    private UserIdentity identity;

    public User() {

    }

    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "password")
    public ByteBuffer getPassword() {
        return password;
    }

    public void setPassword(ByteBuffer password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "passwordSalt")
    public ByteBuffer getSalt() {
        return salt;
    }

    public void setSalt(ByteBuffer salt) {
        this.salt = salt;
    }

    @DynamoDBAttribute(attributeName = "identityId")
    public String getCognitoIdentityId() {
        if (this.identity == null) {
            return null;
        }
        return this.identity.getIdentityId();
    }

    public void setCognitoIdentityId(String cognitoIdentityId) {
        if (this.identity == null) {
            this.identity = new UserIdentity();
        }
        this.identity.setIdentityId(cognitoIdentityId);
    }

    @DynamoDBIgnore
    public byte[] getPasswordBytes() {
        return password.array();
    }

    @DynamoDBIgnore
    public byte[] getSaltBytes() {
        return salt.array();
    }

    @DynamoDBIgnore
    public UserIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(UserIdentity identity) {
        this.identity = identity;
    }
}
