package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDialog;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.DAOFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.zarlok.db.dao.ProduktDAO;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.ProductNameFormatter;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.ProduktDataWaznosciFormatter;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import java.util.List;

public class ProductAddPanel extends JPanel {

	private static final long serialVersionUID = 1L;

   private DBManager manager;

	private ProduktDAO produktDAO;

   private FormPanel<MyFormattedTextField> productName;

   private FormPanel<AutocompleteComboBox> jednostka;

   private FormPanel<MyFormattedTextField> dataWazn;

	private FormFactory<Produkt> factory = new  FormFactory<Produkt>();

	private DAOForm<Produkt, DAOFormElement> form;

	private ProductNameFormatter formatter;

//	private OkButtonPanel<Produkt> buttonPanel;

	public ProductAddPanel(){
		super();
	}

	public void initialize() {
      MyFormattedTextField productNameField = new MyFormattedTextField();
		productNameField.setSeparateuserTextAndValue(false);
		productNameField.setEchoErrorsAtOnce(true);
		productNameField.setFormatter(new ProductNameFormatter(manager.createEntityManager()));
		formatter = (ProductNameFormatter) productNameField.getFormatter();
		productName = factory.decorateFormattedTextField("Nazwa", "nazwa", productNameField);
      AutocompleteComboBox jednostkaBox = new ProduktJednostkaComboBox(manager);
		jednostka = factory.decotrateComboBox("Jednostka", "jednostka", jednostkaBox);
      MyFormattedTextField dataWaznTextField = new MyFormattedTextField(new ProduktDataWaznosciFormatter());
		dataWazn = factory.decorateFormattedTextField("Data ważności", "dataWaznosci", dataWaznTextField);
		form = factory.getCreatedForm();
		form.setDao(getProduktDAO());
		initGUI();
	}

	private void initGUI() {
		setLayout(new MigLayout("wrap 1, fillx", "[fill]"));
		add(productName);
		add(jednostka);
		add(dataWazn);
//		add(buttonPanel);
	}


   public void setManager(DBManager manager) {
		this.manager = manager;
	}

	public ProduktDAO getProduktDAO() {
		if(produktDAO==null){
			produktDAO = new ProduktDAO(manager);
		}
		return produktDAO;
	}

	public void requestFocus(){
		productName.requestFocus();
	}

	public void setProdukt(Produkt p) {
      if(p==null){
         throw new IllegalArgumentException();
      }
		form.setEntity(p);
		formatter.setProd(p);
	}

	public boolean commit() {
      List<Object> errors = form.checkErrors();
      if(!errors.isEmpty()){
         ErrorDialog.displayErrorDialog(errors, Utils.getFrameForComponent(this));
         return false;
      }
		form.commit();
      return true;
	}

	public void startEditing() {
		form.startEditing();
	}

	public void startViewing() {
		form.startViewing();
	}

	public void stopEditing() {
		form.rollback();
	}

	public DAOForm<Produkt, DAOFormElement> getForm() {
		return form;
	}


}
