package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.ui.query.FullTextPanel;
import cx.ath.jbzdak.jpaGui.ui.query.FulltextFilter;
import cx.ath.jbzdak.jpaGui.ui.table.TableButtonRendererEditor;
import cx.ath.jbzdak.zarlock.ui.ZarlockModel;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;
import cx.ath.jbzdak.zarlok.entities.Product;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getZarlockBundle;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-17
 */
public class ProductList extends JPanel{

   FulltextFilter<TableModel, Integer> filter = new FulltextFilter<TableModel, Integer>(1,2);

   FullTextPanel fullTextPanel = new FullTextPanel(filter, getZarlockBundle());

   JXTable productTable;

   List<Product> products = Collections.emptyList();

   TableButtonRendererEditor editor = new TableButtonRendererEditor();

   public ProductList() {
      super(new MigLayout("wrap 1, fillx", "[grow, fill]", "[|grow, fill]"));
      productTable = new JXTable();
      TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>();
      rowSorter.setRowFilter(filter);
      productTable.setRowSorter(rowSorter);
      add(fullTextPanel);
      add(new JScrollPane(productTable));
      JButton goToDetails = Utils.createIconButton(ICON_MANAGER.getIcon("product.go"));
      goToDetails.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ZarlockModel model = ZarlockUtils.getZarlockModel(ProductList.this);
            model.showProductDetails((Product) editor.getValue());
         }
      });
      editor.add(goToDetails, BorderLayout.CENTER);
      initBindings();
   }

   public void setProducts(List<Product> products) {
      List<Product> oldProducts = this.products;
      this.products = products;
      firePropertyChange("products", oldProducts, this.products);
   }

   public List<Product> getProducts() {
      return products;
   }

   

   public void initBindings(){
      JTableBinding binding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ,
              this, BeanProperty.<ProductList, List<Product>>create("products"), productTable);
      binding.addColumnBinding(BeanProperty.create("name")).setColumnName(getString("product.name"));
      binding.addColumnBinding(BeanProperty.create("unit")).setColumnName(getString("product.unit"));
      binding.addColumnBinding(BeanProperty.create("expiryDate")).setColumnName(getString("product.expiryDate"));
      binding.addColumnBinding(ObjectProperty.create()).setColumnName( getString("productList.goToProduct"));
      binding.bind();
      TableButtonRendererEditor renderer = new TableButtonRendererEditor();
      renderer.add(Utils.createIconButton(ICON_MANAGER.getIcon("product.go")), BorderLayout.CENTER);
      productTable.getColumnModel().getColumn(3).setCellEditor(editor);
      productTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
   }

}
