package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.ReturnableTransaction;
import cx.ath.jbzdak.jpaGui.db.JPAReturnableTransaction;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigDecimal;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class KartotekaPanelModel {

   final Product product;

   List<KartotekaEntry> entries = new ArrayList<KartotekaEntry>();

   public KartotekaPanelModel(Product product) {
      this.product = product;
      initEntries();
   }

   void initEntries(){
      DBManager<EntityManager> manager = DBHolder.getDbManager();
      List<Object[]> names = manager.executeTransaction(new JPAReturnableTransaction<List<Object[]>>(){
         @Override
         public List<Object[]> doTransaction(EntityManager entityManager) throws Exception {
            return entityManager.createQuery("SELECT DISTINCT psc.specifier, psc.unit FROM ProductSearchCache psc WHERE psc.productName = :name")
                    .setParameter("name", product.getName()).getResultList();
         }
      });
      for(Object[] name : names){
         String specifier = (String) name[0];
         String unit = (String) name[1];
         entries.add(new KartotekaEntry(specifier, unit, fetchBeansForSpecifier(specifier, unit)));
      }
   }

   static void calculatBeans(List<KartotekaBean> beans){
      Collections.sort(beans);
      BigDecimal currentValue= BigDecimal.ZERO, currentQuantity = BigDecimal.ZERO;
      for(KartotekaBean kartotekaBean : beans){
         currentQuantity = currentQuantity.add(kartotekaBean.addedToStock);
         currentValue = currentValue.add(kartotekaBean.value);
         kartotekaBean.stockInWarehouse = currentQuantity;
         kartotekaBean.valueInWarehouse = currentValue;
      }
   }

   List<KartotekaBean> fetchBeansForSpecifier(final String specifier, final String unit){
      DBManager<EntityManager> manager = DBHolder.getDbManager();
      return manager.executeTransaction(new ReturnableTransaction<EntityManager, List<KartotekaBean> >() {
         @Override
         public List<KartotekaBean>  doTransaction(EntityManager entityManager) throws Exception {
            List<KartotekaBean> result = new ArrayList<KartotekaBean>();
            List<KartotekaBean> batches =
                    entityManager.createQuery("SELECT new cx.ath.jbzdak.zarlock.ui.product.KartotekaBean(b) " +
                            "FROM Batch b WHERE b.specifier = :specifier AND b.unit = :unit AND b.product = :product")
                    .setParameter("specifier", specifier)
                    .setParameter("unit", unit)
                    .setParameter("product", product).getResultList();
            List<KartotekaBean> expenditures =
                  entityManager.createQuery("SELECT new cx.ath.jbzdak.zarlock.ui.product.KartotekaBean(e) " +
                            "FROM Batch b, IN(b.expenditures) e " +
                          "WHERE b.specifier = :specifier AND b.unit = :unit " +
                          "AND b.product = :product")
                    .setParameter("specifier", specifier)
                    .setParameter("unit", unit) .setParameter("product", product).getResultList();
            List<KartotekaBean> planned =
                     entityManager.createQuery("SELECT new cx.ath.jbzdak.zarlock.ui.product.KartotekaBean(pe) " +
                            "FROM PlannedExpenditure pe  " +
                             "WHERE pe.product = :product AND pe.specifier = :specifier " +
                             "AND pe.unit = :unit AND pe.spent = false")
                    .setParameter("specifier", specifier)
                    .setParameter("unit", unit) .setParameter("product", product).getResultList();
            result.addAll(planned);
            result.addAll(expenditures);
            result.addAll(batches);
            calculatBeans(result);
            return result;
         }
      });
   }
}
