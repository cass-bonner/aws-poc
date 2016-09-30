package poc.sa.ms.dao.order;

public class DaoFactory {
	
	public enum DaoType {
		DynamoDb;
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
