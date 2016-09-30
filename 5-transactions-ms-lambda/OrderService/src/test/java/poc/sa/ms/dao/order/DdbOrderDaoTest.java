package poc.sa.ms.dao.order;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poc.sa.ms.common.model.order.Item;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.common.model.order.payment.CreditCardPaymentDetail;
import poc.sa.ms.common.model.order.payment.CreditCardProvider;
import poc.sa.ms.dao.order.DdbOrderDao;

public class DdbOrderDaoTest {

  DdbOrderDao ddbOrderDao = new DdbOrderDao();

  @Before
  public void setup() {

  }

  /**
   * Throws exception since all order population is not complete
   */
  @Test(expected = RuntimeException.class)
  public void testValdateMissingPaymentAndItems() {
    Order order = new Order("fred", null, null);
    ddbOrderDao.validate(order);
  }

  /**
   * Throws exception since all order population is not complete
   */
  @Test(expected = RuntimeException.class)
  public void testValdateMissingPayment() {
    Item item = new Item("XXX-1234", "Ferrari", "Car", "Italia", 1);
    List<Item> items = new ArrayList<Item>();
    items.add(item);
    Order order = new Order("fred", items, null);
    ddbOrderDao.validate(order);
  }

  /**
   * Throws exception since all order population is not complete
   */
  @Test(expected = RuntimeException.class)
  public void testValdateItemsNull() {
    
    CreditCardPaymentDetail paymentDetail = new CreditCardPaymentDetail(
        "MASTERCARD",
        "1234567890",
        new Date(),
        "233",
        new BigDecimal(57.32));

    Order order = new Order("fred", null, paymentDetail);
    ddbOrderDao.validate(order);
  }

  /**
   * Throws exception since all order population is not complete
   */
  @Test(expected = RuntimeException.class)
  public void testValdateItemsEmpty() {
    
    CreditCardPaymentDetail paymentDetail = new CreditCardPaymentDetail(
        CreditCardProvider.MASTERCARD.toString(),
        "1234567890",
        new Date(),
        "233",
        new BigDecimal(57.32));
    List<Item> items = new ArrayList<Item>();
    Order order = new Order("fred", items, paymentDetail);
    ddbOrderDao.validate(order);
  }
  
  /**
   * No exception since all order population is  omplete
   */
  @Test
  public void testValdateValid() {
    
    CreditCardPaymentDetail paymentDetail = new CreditCardPaymentDetail(
        CreditCardProvider.MASTERCARD.toString(),
        "1234567890",
        new Date(),
        "233",
        new BigDecimal(57.32));
    Item item = new Item("XXX-1234", "Ferrari", "Car", "Italia", 1);
    List<Item> items = new ArrayList<Item>();
    items.add(item);
    Order order = new Order("fred", items, paymentDetail);
    ddbOrderDao.validate(order);
    //confirm no exception thrown/order is valid.
    assertNotNull(order);
  }

}
