package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.Formatter;
import cx.ath.jbzdak.jpaGui.FormattingException;
import cx.ath.jbzdak.jpaGui.BeanHolderAware;
import cx.ath.jbzdak.jpaGui.BeanHolder;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.ReturnableTransaction;
import cx.ath.jbzdak.jpaGui.db.JPAReturnableTransaction;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.Product;

import javax.persistence.EntityManager;
import java.util.List;
import java.awt.event.ActionListener;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductNameFormatter implements Formatter<String,  String>, BeanHolderAware<Product, BeanHolder<Product>>{

   BeanHolder<Product> beanHolder;

   public void setBeanHolder(BeanHolder<Product> beanHolder) {
      this.beanHolder = beanHolder;
   }

   @Override
   public String parseValue(final String text) throws Exception {
      DBManager<EntityManager> manager = DBHolder.getDbManager();
      List<Long> id =  manager.executeTransaction(new JPAReturnableTransaction<List<Long>>(){
         @Override
         public List<Long> doTransaction(EntityManager entityManager) throws Exception {
            return entityManager.createNamedQuery("getProductIdByName").setParameter("name", text).getResultList();
         }
      });
      if(id.isEmpty() || id.get(0).equals(beanHolder.getBean().getId())){
         return text;
      }
      throw new UnsupportedOperationException("Nie można zmienić nazwy produktu na nazwę innego produktu");
   }

   @Override
   public String formatValue(String value) throws FormattingException {
      return value; 
   }

   @Override
   public void addFormatterChangedListener(ActionListener actionListener) {

   }

   @Override
   public void removeFormatterChangedListener(ActionListener actionListener) {

   }
}
