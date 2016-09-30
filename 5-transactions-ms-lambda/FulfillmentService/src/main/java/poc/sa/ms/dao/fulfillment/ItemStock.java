package poc.sa.ms.dao.fulfillment;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import poc.sa.ms.common.model.config.DdbConfiguration;

@DynamoDBTable(tableName = DdbConfiguration.STOCK_TABLE_NAME)
public class ItemStock {
  @DynamoDBHashKey(attributeName = "sku")
  private String sku;
  @DynamoDBAttribute(attributeName = "quantity")
  private int quantity;
  @DynamoDBAttribute(attributeName = "reOrdered")
  private boolean reOrdered;
  @DynamoDBAttribute(attributeName = "updateDate")
  private Date updateDate;
  public String getSku() {
    return sku;
  }
  public void setSku(String sku) {
    this.sku = sku;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public Date getUpdateDate() {
    return updateDate;
  }
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
  public boolean isReOrdered() {
    return reOrdered;
  }
  public void setReOrdered(boolean reOrdered) {
    this.reOrdered = reOrdered;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + quantity;
    result = prime * result + (reOrdered ? 1231 : 1237);
    result = prime * result + ((sku == null) ? 0 : sku.hashCode());
    result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
    ItemStock other = (ItemStock) obj;
    if (quantity != other.quantity)
      return false;
    if (reOrdered != other.reOrdered)
      return false;
    if (sku == null) {
      if (other.sku != null)
        return false;
    } else if (!sku.equals(other.sku))
      return false;
    if (updateDate == null) {
      if (other.updateDate != null)
        return false;
    } else if (!updateDate.equals(other.updateDate))
      return false;
    return true;
  }
  @Override
  public String toString() {
    return "ItemStock [sku=" + sku + ", quantity=" + quantity + ", reOrdered=" + reOrdered + ", updateDate="
        + updateDate + "]";
  }
  
}
