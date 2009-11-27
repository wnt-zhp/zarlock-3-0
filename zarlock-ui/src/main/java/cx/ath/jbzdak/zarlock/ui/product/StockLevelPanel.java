package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.entities.misc.StockLevelBean;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
class StockLevelPanel extends JPanel{

   JTable stanTable = new JTable();

   StockLevelPanelModel stanPanelModel = new StockLevelPanelModel();

   public StockLevelPanel() {
      super(new BorderLayout());
      add(new JScrollPane(stanTable), BorderLayout.CENTER);
      initBindings();
   }

   private void initBindings(){
      JTableBinding binding
              = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ,
              stanPanelModel,  BeanProperty.<StockLevelPanelModel, List<StockLevelBean>>create("stockLevels"), stanTable);
      binding.addColumnBinding(BeanProperty.create("specifier"))
              .setEditable(false)
              .setColumnName(getString("batch.specifier"));
      binding.addColumnBinding(BeanProperty.create("unit"))
              .setEditable(false)
              .setColumnName(getString("product.unit"));
      binding.addColumnBinding(BeanProperty.create("quantity"))
              .setEditable(false)
              .setColumnName(getString("stockLevel.quantity"));
      binding.bind();
   }

   public void setProduct(Product product) {
      stanPanelModel.setProduct(product);
   }
}
