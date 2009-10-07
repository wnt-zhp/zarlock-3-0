package cx.ath.jbzdak.zarlok.ui.wyprowadzenie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.table.TableObjectProperty;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jun 28, 2009
 */
public class WyprowadzenieEditTable extends JTable{

   private final WyprowadzeniaEditTableModel model;

   public WyprowadzenieEditTable(DBManager dbManager) {
      this.model = new WyprowadzeniaEditTableModel(dbManager, Wyprowadzenie.class, this);

      JTableBinding binding = SwingBindings.<Wyprowadzenie, WyprowadzeniaEditTableModel>createJTableBinding(AutoBinding.UpdateStrategy.READ, model, (Property) BeanProperty.create("entities"), this);
      binding.addColumnBinding(BeanProperty.create("partia.searchFormat")).setColumnName("Nazwa partii").setEditable(false);
      binding.addColumnBinding(BeanProperty.create("data")).setColumnName("Data").setEditable(false);
      binding.addColumnBinding(ELProperty.create("#{iloscJednostek} #{partia.jednostka}")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("tytulem")).setColumnName("Tytułem").setEditable(false);
      binding.addColumnBinding(new TableObjectProperty()).setColumnName("Akcje").setEditable(true);
      //binding.addBindingListener(new DebugBindingListener());
      model.setInsertNewRow(false);
      model.setEntities(Arrays.asList(new Wyprowadzenie()));
      binding.bind();
      model.setEntities(Collections.<Wyprowadzenie>emptyList());
      TableColumn akcjeColumn = getColumnModel().getColumn(4);
      akcjeColumn.setCellEditor(model.createRendererEditor());
      akcjeColumn.setCellRenderer(model.createRendererEditor());
      setRowHeight(30);
   }

   public void setEntities(List<Wyprowadzenie> entities) {

      List<Wyprowadzenie> wyprowadzenies = model.getEntities();
      model.setEntities(entities);
      firePropertyChange("entities", model.getEntities(), wyprowadzenies);

   }
}
