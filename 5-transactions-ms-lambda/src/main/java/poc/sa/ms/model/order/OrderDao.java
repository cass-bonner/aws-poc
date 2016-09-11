package poc.sa.ms.model.order;

import java.util.List;

import poc.sa.ms.exception.DaoException;

public interface OrderDao {
	
	String createOrder(Order order) throws DaoException;
	
	Order getOrderById(String orderId) throws DaoException;
	
	List<Order> getOrders(String username) throws DaoException;

}
