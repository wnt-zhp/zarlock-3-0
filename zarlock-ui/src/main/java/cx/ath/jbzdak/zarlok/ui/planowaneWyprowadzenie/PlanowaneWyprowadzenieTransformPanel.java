package cx.ath.jbzdak.zarlok.ui.planowaneWyprowadzenie;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.autoComplete.UnwrapConverter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelConstraints;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelMock;
import cx.ath.jbzdak.jpaGui.ui.form.ReadOnlyFormElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedFieldElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.table.TablePanel;
import cx.ath.jbzdak.zarlok.entities.PlanowaneWyprowadzenie;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

import javax.swing.*;
import java.util.Map;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-20
 */
public class PlanowaneWyprowadzenieTransformPanel extends JPanel{

   private final PlanowaneTransformModel model;

   @SuppressWarnings({"FieldCanBeLocal"})
   private JLabel planowaneLabel;

   @SuppressWarnings({"FieldCanBeLocal"})
   private JPanel wybierzPartiePanel;

   private JTable wydaneWyprowadzeniaTable;

   private JLabel nazwaProduktuLabel;

   private AutocompleteComboBox specyfikatorComboBox;

   private AutocompleteComboBox jednostkaComboBox;

   private MyFormattedTextField iloscTextField;

   @SuppressWarnings({"FieldCanBeLocal"})
   private JButton sendWyprowadzeniaButton;

   public PlanowaneWyprowadzenieTransformPanel(DBManager dbManager) {
      wydaneWyprowadzeniaTable = new JTable();
      model = new PlanowaneTransformModel(dbManager, wydaneWyprowadzeniaTable);
      initGui();
      initBindings();
   }

   private void initGui(){
      //Komponenty panelu
      setLayout(new MigLayout("grow, fillx, wrap 1", "[]"));
      planowaneLabel = new JLabel();
      wybierzPartiePanel = new JPanel();
      wybierzPartiePanel.setBorder(BorderFactory.createTitledBorder("Co chesz wydać"));
      add(planowaneLabel);
      add(wybierzPartiePanel);
      add(new TablePanel(wydaneWyprowadzeniaTable));
      //Formularz
      Map<String,String> constraints = FormPanelConstraints.createCompactConstraints();
      nazwaProduktuLabel = new JLabel();
      specyfikatorComboBox = new AutocompleteComboBox(model.getSpecyfikatorAdaptor(), true);
      jednostkaComboBox = new AutocompleteComboBox(model.getJednoskaAdaptor(), true);
      iloscTextField = new MyFormattedTextField(model.getIloscFormatter());
      sendWyprowadzeniaButton = new JButton("Wydaj", IconManager.getIconSafe("wydaj"));
      FormPanelMock nazwaProduktu = new FormPanelMock(new ReadOnlyFormElement(nazwaProduktuLabel,"Nazwa produktu"), constraints);
      FormPanelMock specyfikator = new FormPanelMock(new ReadOnlyFormElement(specyfikatorComboBox, "Specyfikator"), constraints);
      FormPanelMock jednostka = new FormPanelMock(new ReadOnlyFormElement(jednostkaComboBox, "Jednostka"), constraints);
      FormPanel iloscField = new FormPanel(new FormattedFieldElement(iloscTextField, "Ilość wydawana"));
      wybierzPartiePanel = new JPanel(new MigLayout("grow, fillx, wrap 4"));
      wybierzPartiePanel.add(nazwaProduktu);
      wybierzPartiePanel.add(specyfikator);
      wybierzPartiePanel.add(jednostka);
      wybierzPartiePanel.add(iloscField);
      wybierzPartiePanel.add(iloscField, "0 3");
   }

   private  void initBindings(){
      BindingGroup bindingGroup = new BindingGroup();
      Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                                                   model, BeanProperty.create("specyfikator"),
                                                   specyfikatorComboBox, BeanProperty.create("selectedItem"));
      binding.setConverter(UnwrapConverter.getInstance());
      bindingGroup.addBinding(binding);
      binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                                                   model, BeanProperty.create("jednostka"),
                                                   jednostkaComboBox, BeanProperty.create("selectedItem"));
      bindingGroup.addBinding(binding);
      binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                                                   model, BeanProperty.create("quantity"),
                                                   iloscTextField, BeanProperty.create("value"));
      bindingGroup.addBinding(binding);
      bindingGroup.bind();
   }

   public void setWyprowadzenie(PlanowaneWyprowadzenie wyprowadzenie) {
      model.setWyprowadzenie(wyprowadzenie);
      nazwaProduktuLabel.setText(wyprowadzenie.getNazwaProduktu());
   }
}
