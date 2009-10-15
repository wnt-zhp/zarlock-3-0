package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.genericListeners.DebugBindingListener;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.ui.iloscOsob.IloscOsobDialog;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Collection;

public class DniTreePanel extends JPanel {

   private static final long serialVersionUID = 1L;

   public JPanel getDetailsPanel() {return model.getDetailsPanel();}

   public void setDni(Collection<Dzien> dni) {model.setDni(dni);}

   public void setEntityManager(EntityManager entityManager) {model.setEntityManager(entityManager);}

   private final DniTreePanelModel model;

   private final IloscOsobDialog iloscOsobDialog;

   @SuppressWarnings({"FieldCanBeLocal"})
   private final JTree tree = new DniTree();

   @SuppressWarnings({"FieldCanBeLocal"})
   private final FormPanel dzienAddPanel;

   @SuppressWarnings({"FieldCanBeLocal"})
   private final JButton addDzienButton = new JButton("Dodaj", IconManager.getIconSafe("dzien_add"));

   @SuppressWarnings({"unchecked", "FieldCanBeLocal"})
   private final
   Binding binding;

   public DniTreePanel() {
      super(new MigLayout("wrap 1", "[grow, fill]", "[grow,fill|]"));
      FormFactory<Dzien> ffactory = new FormFactory<Dzien>();
      ffactory.setLayout("compact");
      dzienAddPanel = ffactory.decorateFormattedTextField("Dodaj dzien", "data", new DateFormatter());
      model = new DniTreePanelModel(ffactory.getCreatedForm(), tree);
      DefaultTreeCellRenderer treeRenderer = new DniTreeRenderer();
      tree.setModel(model.getModel());
      tree.setCellRenderer(treeRenderer);
      tree.setRootVisible(false);
      tree.addTreeSelectionListener(model.new TreeListener());
      tree.addMouseListener(model.new ShowPopupMenuListener(tree));
      add(new JScrollPane(tree), "growx");
      iloscOsobDialog = new IloscOsobDialog(SwingUtilities.getWindowAncestor(this));
      iloscOsobDialog.setModal(true);
      iloscOsobDialog.getButtonPanel().addOkActionTask(model.new IloscOsobDialogActionListener(iloscOsobDialog));
      addDzienButton.addActionListener(model.new AddDzienListener(iloscOsobDialog));
      binding = Bindings
              .createAutoBinding(UpdateStrategy.READ, dzienAddPanel, ELProperty.create("#{not formElement.error}"),
                                 addDzienButton, BeanProperty.create("enabled"));
      binding.addBindingListener(new DebugBindingListener());
      binding.bind();
      add(dzienAddPanel);
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(addDzienButton);
      add(buttonPanel);

   }

   public IloscOsobDialog getIloscOsobDialog() {
      return iloscOsobDialog;
   }
}
