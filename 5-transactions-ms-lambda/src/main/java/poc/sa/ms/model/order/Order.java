package poc.sa.ms.model.order;

import java.util.Date;
import java.util.List;

import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

public final class Order {
  
  public Order(){}
  
  public Order (String userName, 
      List<Item> items, 
      CreditCardPaymentDetail paymentDetail) {
    this.username= userName;
    this.createDate = new Date();
    this.items = items;
    
    this.creditCardPaymentDetail = paymentDetail;
  }
	
	private String orderId;
	
	private String username;
	
	private Date createDate;

	private List<Item> items;
	
	
	// some requirements could support multiple creditCardPaymentDetail types.
	// put types which can be rolled back first. for simplicity
	// we'll keep one type for the poc
	private CreditCardPaymentDetail creditCardPaymentDetail;

	public String getOrderId() {
		return orderId;
	}
	

	public void setOrderId(String orderId) {
    this.orderId = orderId;
  }


  public String getUsername() {
		return username;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public List<Item> getItems() {
		return items;
	}

  public CreditCardPaymentDetail getCreditCardPaymentDetail() {
    return creditCardPaymentDetail;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
    result = prime * result + ((items == null) ? 0 : items.hashCode());
    result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
    result = prime * result + ((creditCardPaymentDetail == null) ? 0 : creditCardPaymentDetail.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Order other = (Order) obj;
    if (createDate == null) {
      if (other.createDate != null)
        return false;
    } else if (!createDate.equals(other.createDate))
      return false;
    if (items == null) {
      if (other.items != null)
        return false;
    } else if (!items.equals(other.items))
      return false;
    if (orderId == null) {
      if (other.orderId != null)
        return false;
    } else if (!orderId.equals(other.orderId))
      return false;
    if (creditCardPaymentDetail == null) {
      if (other.creditCardPaymentDetail != null)
        return false;
    } else if (!creditCardPaymentDetail.equals(other.creditCardPaymentDetail))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", username=" + username + ", createDate=" + createDate + ", items=" + items
        + ", creditCardPaymentDetail=" + creditCardPaymentDetail + "]";
  }

}
