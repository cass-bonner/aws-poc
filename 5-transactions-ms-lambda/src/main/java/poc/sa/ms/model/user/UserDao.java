package poc.sa.ms.model.user;


import poc.sa.ms.common.model.exception.DaoException;

public interface UserDao {
    /**
     * Find a user by its unique username
     *
     * @param username The username to search for
     * @return A populated User object if the user was found, null otherwise
     * @throws DAOException
     */
    User getUserByName(String username) throws DaoException;

    /**
     * Creates a new user in the data store
     *
     * @param user The new user information
     * @return The username of the user that was created
     * @throws DAOException
     */
    String createUser(User user) throws DaoException;
}
