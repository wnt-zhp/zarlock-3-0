package cx.ath.jbzdak.zarlock.ui.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.awt.Component;
import java.util.Collection;
import java.util.List;

import cx.ath.jbzdak.jpaGui.ParsingException;
import cx.ath.jbzdak.zarlock.ui.ZarlockDbAdaptor;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCacheUtils;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 10, 2010
 */
public class ProductSearchCacheAdaptor extends ZarlockDbAdaptor<ProductSearchCache>{

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchCacheAdaptor.class);

   public ProductSearchCacheAdaptor(Component owner) {
      super(owner);
   }

   @Override
   protected Collection<ProductSearchCache> doInBackground(EntityManager manager) {
      ProductSearchCache object;
      try {
         object = ProductSearchCacheUtils.parse(getFilter());
      } catch (ParsingException e) {
         LOGGER.warn("Error while parsing ProductSearchCache", e);
         return null;
      }
      Query query = manager.createNamedQuery("filterProductSearchCache");
      query.setParameter("productName", object.getProductName());
      query.setParameter("specifier", object.getSpecifier());
      query.setParameter("unit", object.getUnit());
      List resultList = query.getResultList();
      LOGGER.debug("Query returned {} results:{}", resultList.size(), resultList);
      return resultList;
   }

  
}
