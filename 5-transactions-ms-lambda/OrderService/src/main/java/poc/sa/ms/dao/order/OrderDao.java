package poc.sa.ms.dao.order;

import java.util.List;

import poc.sa.ms.common.model.exception.DaoException;
import poc.sa.ms.common.model.order.Order;

public interface OrderDao {
	
	String createOrder(Order order) throws DaoException;
	
	Order getOrderById(String orderId) throws DaoException;
	
	List<Order> getOrders(String username) throws DaoException;

}
