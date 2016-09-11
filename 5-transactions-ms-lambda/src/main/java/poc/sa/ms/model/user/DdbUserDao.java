package poc.sa.ms.model.user;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import poc.sa.ms.exception.DaoException;

public class DdbUserDao implements UserDao {
	private static DdbUserDao instance = null;
	
	// Lambda function execution role.
    private static AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();

    /**
     * Returns an initialized instance of the DdbUserDAO object. 
     * Dao objects should be retrieved through the DaoFactory
     * class
     * 
     * NB: not thread safe but should be fine here since we have control over who initiates
     *
     * @return An initialized instance of the DdbUserDAO object
     */
    public static DdbUserDao getInstance() {
        if (instance == null) {
            instance = new DdbUserDao();
        }

        return instance;
    }
	protected DdbUserDao() {
		// protect initiation
	}

	/**
     * Queries DynamoDB to find a user by its Username
     *
     * @param username The username to search for
     * @return A populated User object, null if the user was not found
     * @throws DAOException
     */
	@Override
	public User getUserByName(String username) throws DaoException {
		if (username == null || username.trim().equals("")) {
            throw new DaoException("Cannot lookup null or empty user");
        }

        return getMapper().load(User.class, username);
	}

	@Override
	public String createUser(User user) throws DaoException {
		if (user.getUsername() == null || user.getUsername().trim().equals("")) {
            throw new DaoException("Cannot create user with empty username");
        }

        if (getUserByName(user.getUsername()) != null) {
            throw new DaoException("Username must be unique");
        }

        getMapper().save(user);

        return user.getUsername();
	}

	
	/**
     * Returns a DynamoDBMapper object initialized with the default DynamoDB client
     *
     * @return An initialized DynamoDBMapper
     */
    protected DynamoDBMapper getMapper() {
        return new DynamoDBMapper(ddbClient);
    }
}
