package poc.sa.ms.dao.fulfillment;

public interface InventoryDao {
  /**
   * Returns IN_STOCK, INVALID_SKU, OUT_OF_STOCK, ON_ORDER
   * @param sku
   * @param quantity
   * @return
   */
  public StockStatus checkStockTake(String sku, int quantity);
}
