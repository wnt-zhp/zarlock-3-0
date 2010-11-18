package cx.ath.jbzdak.zarlock.ui.course;

import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;

import javax.swing.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.math.BigDecimal;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import cx.ath.jbzdak.zarlok.entities.*;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import cx.ath.jbzdak.jpaGui.genericListeners.PopupMenuMouseListener;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory2;
import cx.ath.jbzdak.jpaGui.ui.form.SimpleForm;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ParsingException;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlock.ui.DefaultIconManager;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;
import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 30, 2010
 */
public class CoursePanel extends JPanel{

   JTable expenditures = new JTable();

   JTableBinding<Expenditure, CoursePanelModel, JTable> expendituresBinding;

   CoursePanelModel model;

   JPopupMenu expendituresPopupMenu = new JPopupMenu(getString("expenditure"));

   Day day;

   public CoursePanel() {
      model = new CoursePanelModel(this);
      initTable();
      initModel();
      initPopupMenu();
   }

   private void initTable(){
      expenditures = new JTable();
      expendituresBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ,
              model, BeanProperty.<CoursePanelModel, List<Expenditure>>create("course.expenditures"), expenditures);
      expendituresBinding.addColumnBinding(BeanProperty.<Expenditure, String>create("searchFormat"))
              .setColumnName(getString("CoursePanell.expenditureName")).setEditable(false);
      expendituresBinding.addColumnBinding(BeanProperty.<Expenditure, BigDecimal>create("quantity"))
              .setColumnName(getString("expenditure.quantity")).setEditable(false);
      expendituresBinding.bind();
      expenditures.addMouseListener(new PopupMenuMouseListener(expendituresPopupMenu,expenditures));
   }

   private void initPopupMenu(){
      JMenuItem removeExpenditure = new JMenuItem(getString("CoursePanel.removeExpenditure"), ICON_MANAGER.getIcon("cancel"));
      removeExpenditure.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int selectedRow = expenditures.getSelectedRow();
            if(selectedRow == -1){
               JOptionPane.showMessageDialog(CoursePanel.this,
                       getString("CoursePanel.removeExpenditure.selectExpenditure"),
                       getString("default.error"),
                       JOptionPane.ERROR_MESSAGE);
               return;
            }
            selectedRow = expenditures.convertRowIndexToModel(selectedRow);
            model.removeExpenditure(selectedRow);
         }
      });

   }

   private void initModel(){
      model.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            expendituresBinding.refresh();
         }
      });
   }


   class BatchSelectPanel{

      AutocompleteComboBox<Batch> batchComboBox;

      PatternBeanFormatter patternBeanFormatter =
              new PatternBeanFormatter("<html><emph>(max. {0})</emph></html>");

      FormPanel batch;

      JLabel maxQuantity; 

      FormPanel quantity;

      JButton addButton;

      BatchSelectPanel() {
         batchComboBox = new AutocompleteComboBox<Batch>(new BatchSelectAdapter(ZarlockUtils.getZarlockModel(CoursePanel.this).getDBManager()));
         batchComboBox.addPropertyChangeListener("selectedItem", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
               Batch b = (Batch) evt.getNewValue();
               if(b==null){
                  maxQuantity.setText("");
               }else{
//                  maxQuantity.setT
               }

            }
         });
      }

   }

   class BatchSelectAdapter extends DbAdaptor<Batch>{

      BatchSelectAdapter(DBManager<EntityManager> dbManager) {
         super(dbManager);
      }

      @Override
      protected Collection<Batch> doInBackground(EntityManager manager) {
         ProductSearchCache cache;
         try {
            cache = ProductSearchCacheUtils.parse(getFilter());
         } catch (ParsingException e) {
            return Collections.emptyList();
         }
         Query query = manager.createNamedQuery("getBatchesForCourse");
         query.setParameter("name", cache.getProductName());
         query.setParameter("specifier", cache.getSpecifier());
         query.setParameter("unit", cache.getUnit());
         query.setParameter("dzien", day.getDate());
         return query.getResultList();
      }
   }
}





