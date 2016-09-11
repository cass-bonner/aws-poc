package poc.sa.ms.model.action;

import java.util.List;

import poc.sa.ms.model.order.Item;
import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

public class CreateOrderRequest {
	private String username;
	private List<Item> items;
	private CreditCardPaymentDetail creditCardPaymentDetail;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
  public CreditCardPaymentDetail getPaymentDetail() {
    return creditCardPaymentDetail;
  }
  public void setPaymentDetail(CreditCardPaymentDetail creditCardPaymentPaymentDetail) {
    this.creditCardPaymentDetail = creditCardPaymentPaymentDetail;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((items == null) ? 0 : items.hashCode());
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
    CreateOrderRequest other = (CreateOrderRequest) obj;
    if (items == null) {
      if (other.items != null)
        return false;
    } else if (!items.equals(other.items))
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
    return "CreateOrderRequest [username=" + username + ", items=" + items + ", creditCardPaymentDetail=" + creditCardPaymentDetail + "]";
  }

	

}
