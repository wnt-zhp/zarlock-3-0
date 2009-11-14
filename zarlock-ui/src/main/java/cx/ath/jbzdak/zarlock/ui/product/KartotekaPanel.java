package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Product;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import javax.swing.*;
import java.util.List;
import java.awt.*;

import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class KartotekaPanel extends JPanel {


   private KartotekaPanelModel model;

   private final PatternBeanFormatter tabTitleFormatter = new PatternBeanFormatter("{specifier} - {unit}");

   private JTabbedPane jTabbedPane = new JTabbedPane();

   public KartotekaPanel(Product product) {
      setLayout(new BorderLayout());
      model = new KartotekaPanelModel(product);
      setBorder(BorderFactory.createTitledBorder(getString("kartoteka")));
      for(KartotekaEntry entry : model.entries){
         jTabbedPane.addTab(tabTitleFormatter.format(entry), new KartotekaTab(entry.getKartotekaEntryList()));
      }
      add(jTabbedPane, BorderLayout.CENTER);

   }

   private class KartotekaTab extends JPanel{

      List<KartotekaBean> kartotekaBeans;

      private JXTable kartoteka;

      private KartotekaTab(List<KartotekaBean> kartotekaBeans) {
         super(new BorderLayout());
         this.kartotekaBeans = kartotekaBeans;
         kartoteka = new JXTable();
         kartoteka.addHighlighter(new ColorHighlighter(new Predicate(KartotekaType.BATCH), Color.GREEN, null));
         kartoteka.addHighlighter(new ColorHighlighter(new Predicate(KartotekaType.EXPENDITURE), Color.PINK, null));
         kartoteka.addHighlighter(new ColorHighlighter(new Predicate(KartotekaType.PLANNED), Color.YELLOW, null));
         add(new JScrollPane(kartoteka));
         initBindings();
      }

      private void initBindings() {
         JTableBinding binding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_ONCE,
                 kartotekaBeans, kartoteka);
         binding.addColumnBinding(BeanProperty.create("date"), getString("kartoteka.dateOfChange"));
         binding.addColumnBinding(BeanProperty.create("addedToStock"), getString("kartoteka.addedToStock"));
         binding.addColumnBinding(BeanProperty.create("price"), getString("kartoteka.price"));
         binding.addColumnBinding(BeanProperty.create("value"), getString("kartoteka.value"));
         binding.addColumnBinding(BeanProperty.create("stockInWarehouse"), getString("kartoteka.stockInWarehouse"));
         binding.addColumnBinding(BeanProperty.create("valueInWarehouse"), getString("kartoteka.valueInWarehouse"));
         binding.addColumnBinding(BeanProperty.create("tytulem"), getString("kartoteka.tytulem"));
         binding.bind();
      }

      private class Predicate implements HighlightPredicate{

         final KartotekaType type;

         private Predicate(KartotekaType type) {
            this.type = type;
         }

         @Override
         public boolean isHighlighted(Component component, ComponentAdapter componentAdapter) {
            return type == kartotekaBeans.get(kartoteka.convertRowIndexToModel(componentAdapter.row)).kartotekaType;
         }
      }
   }

}
