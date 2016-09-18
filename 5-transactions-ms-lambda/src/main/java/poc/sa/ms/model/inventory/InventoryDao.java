package poc.sa.ms.model.inventory;

import java.util.List;

public interface InventoryDao {
  /**
   * Returns IN_STOCK, INVALID_SKU, OUT_OF_STOCK, ON_ORDER
   * @param sku
   * @param quantity
   * @return
   */
  public StockStatus checkStockTake(String sku, int quantity);
  public List<String> getSkus();
}
