package poc.sa.ms.model.order;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class DdbCatalogItemDaoTest {

  @Test
  public void test() {
    CatalogItem catalogItem = new CatalogItem();
    catalogItem.setId(102);
    catalogItem.setTitle("Book 102 Title");
    catalogItem.setISBN("222-2222222222");
    catalogItem.setBookAuthors(new HashSet<String>(Arrays.asList("Author 1", "Author 2")));
    
    DdbCatalogItemDao ddbCatalogItemDao = new DdbCatalogItemDao();
    ddbCatalogItemDao.createCatalogItem(catalogItem);
    
  }

}
