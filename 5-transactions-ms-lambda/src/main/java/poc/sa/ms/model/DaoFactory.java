package poc.sa.ms.model;

import poc.sa.ms.model.order.DdbOrderDao;
import poc.sa.ms.model.order.OrderDao;
import poc.sa.ms.model.user.DdbUserDao;
import poc.sa.ms.model.user.UserDao;

public class DaoFactory {
	
	public enum DaoType {
		DynamoDb;
	}
	
	/**
     * Returns the default UserDAO object
     *
     * @return The default implementation of the UserDAO object - by default this is the DynamoDB implementation
     */
    public static UserDao getUserDao() {
        return getUserDao(DaoType.DynamoDb);
    }

    /**
     * Returns a UserDAO implementation
     *
     * @param daoType A value from the DAOType enum
     * @return The corresponding UserDAO implementation
     */
    public static UserDao getUserDao(DaoType daoType) {
        UserDao dao = null;
        switch (daoType) {
            case DynamoDb:
                dao = DdbUserDao.getInstance();
                break;
        }

        return dao;
    }

    /**
     * Returns the default OrderDao implementation
     *
     * @return The DynamoDB OrderDao implementation
     */
	public static OrderDao getOrderDao() {
		return getOrderDao(DaoType.DynamoDb);
	}
	
	/**
     * Returns a OrderDao implementation
     *
     * @param daoType A value from the DaoType enum
     * @return The corresponding OrderDao implementation
     */
    public static OrderDao getOrderDao(DaoType daoType) {
        OrderDao dao = null;
        switch (daoType) {
            case DynamoDb:
                dao = DdbOrderDao.getInstance();
                break;
        }

        return dao;
    }

}
