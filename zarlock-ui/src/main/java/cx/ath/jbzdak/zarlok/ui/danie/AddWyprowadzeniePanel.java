package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.autoComplete.ComboBoxElement;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelConstraints;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedFieldElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jacek Bzdak jbzdak@gmail.com
 * Date: 2009-04-15
 * Time: 02:34:08
 * @author
 */
public class AddWyprowadzeniePanel extends JPanel{

    private final AddWyprowadzeniePanelModel model;

    private final AutocompleteComboBox partiaBox;

    private final MyFormattedTextField iloscField;
//
    private final JButton sendWyprowadzeniaButton;

    private final PatternBeanFormatter maxIloscFormatter
            = new PatternBeanFormatter("<html><em>(max. {0} {1.jedn})</em></html>");
    private final JLabel maxQuantity;

   @SuppressWarnings("unchecked")
    public AddWyprowadzeniePanel(DBManager dbManager, WyprowadzeniaTable wyprowadzeniaTable) {
        super(new MigLayout("fillx", "[fill,grow||]"));
        this.model = new AddWyprowadzeniePanelModel(dbManager, wyprowadzeniaTable);
        partiaBox = new AutocompleteComboBox(model.new Adapter(dbManager));
        partiaBox.setStrict(true);
        iloscField = new MyFormattedTextField(model.new IloscJednostekFormatter());
        FormPanel<AutocompleteComboBox> boxFormPanel;

        boxFormPanel = new FormPanel<AutocompleteComboBox>(new ComboBoxElement(partiaBox, "Wybierz partię","insertedProduct"),
            FormPanelConstraints.createCompactConstraints());
        Map<String, String> formPanelConstraints =
                FormPanelConstraints.createCompactConstraints();
        FormPanel<MyFormattedTextField> textFieldFormPanel
            = new FormPanel(new FormattedFieldElement(iloscField, "Wprowadź ilość", "quantiy"), formPanelConstraints);
        maxQuantity = new JLabel("");
        sendWyprowadzeniaButton = new JButton("Dodaj wyprowadzenia", IconManager.getIconSafe("wydaj"));
        sendWyprowadzeniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.sendWyprowadzenia();
            }
        });
        add(boxFormPanel);
        add(textFieldFormPanel);
        add(maxQuantity);
        add(sendWyprowadzeniaButton);
        initBindings();
        setBorder(BorderFactory.createTitledBorder("Dodaj wyprowadzenie"));
    }

   @SuppressWarnings("unchecked")
    private void initBindings(){
        AutoBinding partiaBoxBinding =
                Bindings.createAutoBinding(UpdateStrategy.READ, partiaBox,
                        BeanProperty.create("selectedItem"), model, BeanProperty.create("insertedProduct"));
        partiaBoxBinding.setConverter(new Converter() {
            @Override
            public Object convertForward(Object value) {
                if (value instanceof AutoCompleteValueHolder) {
                    AutoCompleteValueHolder autoCompleteValueHolder = (AutoCompleteValueHolder) value;
                    return autoCompleteValueHolder.getValue();
                }
                return value;
            }

            @Override
            public Object convertReverse(Object value) {
                return value;
            }
        });
        partiaBoxBinding.bind();
        AutoBinding quantityBinding =
                Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, iloscField,
                        BeanProperty.create("value"), model, BeanProperty.create("quantity"));
        quantityBinding.bind();
        model.addPropertyChangeListener("maxQuantity", new MyPropertyChangeListener());
        model.addPropertyChangeListener("insertedProduct", new MyPropertyChangeListener());
        partiaBox.addPropertyChangeListener("error", new EnabledListener());
        iloscField.addPropertyChangeListener("value", new EnabledListener());


    }

    public AddWyprowadzeniePanelModel getModel() {
        return model;
    }

    public AutocompleteComboBox getPartiaBox() {
        return partiaBox;
    }

    public MyFormattedTextField getIloscField() {
        return iloscField;
    }



    public void setDzien(Dzien dzien) {
        model.setDzien(dzien);
    }

       private class MyPropertyChangeListener implements PropertyChangeListener {
        @Override
            public void propertyChange(PropertyChangeEvent evt) {
            if(model.getMaxQuantity()!=null && model.getInsertedProduct()!=null){
                maxQuantity.setText(maxIloscFormatter.format(model.getMaxQuantity(), model.getInsertedProduct()));
            }else{
                maxQuantity.setText("");
            }
        }
    }

    private class EnabledListener implements PropertyChangeListener {
        @Override
            public void propertyChange(PropertyChangeEvent evt) {
            sendWyprowadzeniaButton.setEnabled((!partiaBox.isError() && iloscField.getValue()!=null && ((Number)iloscField.getValue()).doubleValue()>0));
        }
    }
}
