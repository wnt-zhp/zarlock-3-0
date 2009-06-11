package cx.ath.jbzdak.zarlok.ui.planowaneWyprowadzenie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.PlanowaneWyprowadzenie;
import javax.swing.JList;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-17
 */
public class PlanowaneWyprowazeniaList extends JList{

   @SuppressWarnings({"FieldCanBeLocal"})
   private final DBManager dbManager;

   private final DAO<Danie> danieDao;

   private final List<PlanowaneWyprowadzenie> planowaneWyprowadzenia
           = ObservableCollections.observableList(new ArrayList());

   @SuppressWarnings({"FieldCanBeLocal"})
   private final JListBinding listBinding;

   public PlanowaneWyprowazeniaList(DBManager dbManager) {
      this.dbManager = dbManager;
      this.danieDao = dbManager.getDao(Danie.class);
      listBinding = initBindings(this);
   }

   private static JListBinding initBindings(PlanowaneWyprowazeniaList list){
      JListBinding binding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ,
                                                              list.planowaneWyprowadzenia, list);
      binding.setDetailBinding(BeanProperty.create("searchFormat"));
      binding.bind();
      return binding;
   }

   public void setDanie(Danie entity) {
      danieDao.setEntity(entity);
      planowaneWyprowadzenia.clear();;
      planowaneWyprowadzenia.addAll(entity.getPlanowaneWyprowadzenia());
   }

   public Danie getDanie() {return danieDao.getEntity();}
}
