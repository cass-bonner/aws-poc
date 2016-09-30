package poc.sa.ms.common.model.order;

public class Item {
	
	private  String sku;
	private  String lineItemDescription;
	private  String lineItemType;
	private  String supplierName;
	private  int quantity;
	
	public Item(){};
	public Item(String sku,
	    String lineItemDescription,
	    String lineItemType,
	    String supplierName,
	    int quantity) {
	  this.lineItemDescription = lineItemDescription;
	  this.lineItemType = lineItemType;
	  this.supplierName = supplierName;
	  this.quantity = quantity;
	}
	
	public String getSku() {
    return sku;
  }
  public void setSku(String sku) {
    this.sku = sku;
  }
  public String getLineItemType() {
		return lineItemType;
	}
	public int getQuantity() {
		return quantity;
	}
  public String getSupplierName() {
    return supplierName;
  }
  public String getLineItemDescription() {
    return lineItemDescription;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((lineItemDescription == null) ? 0 : lineItemDescription.hashCode());
    result = prime * result + ((lineItemType == null) ? 0 : lineItemType.hashCode());
    result = prime * result + quantity;
    result = prime * result + ((sku == null) ? 0 : sku.hashCode());
    result = prime * result + ((supplierName == null) ? 0 : supplierName.hashCode());
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
    Item other = (Item) obj;
    if (lineItemDescription == null) {
      if (other.lineItemDescription != null)
        return false;
    } else if (!lineItemDescription.equals(other.lineItemDescription))
      return false;
    if (lineItemType == null) {
      if (other.lineItemType != null)
        return false;
    } else if (!lineItemType.equals(other.lineItemType))
      return false;
    if (quantity != other.quantity)
      return false;
    if (sku == null) {
      if (other.sku != null)
        return false;
    } else if (!sku.equals(other.sku))
      return false;
    if (supplierName == null) {
      if (other.supplierName != null)
        return false;
    } else if (!supplierName.equals(other.supplierName))
      return false;
    return true;
  }
  @Override
  public String toString() {
    return "Item [sku=" + sku + ", lineItemDescription=" + lineItemDescription + ", lineItemType=" + lineItemType
        + ", supplierName=" + supplierName + ", quantity=" + quantity + "]";
  }
  
  
  
}
