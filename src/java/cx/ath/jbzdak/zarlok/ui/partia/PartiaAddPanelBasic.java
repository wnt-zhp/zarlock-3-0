package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.DAOFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.NotEmptyFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.zarlok.db.dao.PartiaDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.JednostkaAdaptor;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.SpecyfikatorAdaptor;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.CenaFormatter;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.DataWaznosciFormatter;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.IloscPoczFormatter;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class PartiaAddPanelBasic extends JPanel{

	private static final long serialVersionUID = 1L;

	protected final DBManager manager;

	protected final DAOForm<Partia, ? extends DAOFormElement> form;

	protected final FormFactory<Partia> factory = new FormFactory<Partia>();

	protected FormPanel<JTextField> produktNamePanel;

	protected FormPanel<AutocompleteComboBox> produktPanel;

	protected FormPanel<AutocompleteComboBox> jednostkaPanel;

	protected FormPanel<AutocompleteComboBox> specyfikatorPanel;

	protected FormPanel<MyFormattedTextField> iloscPoczatkowaPanel;

	protected FormPanel<MyFormattedTextField> cenaPanel;

	protected FormPanel<MyFormattedTextField> dataWaznosciPanel;

	protected FormPanel<MyFormattedTextField> dataksiegowaniaPanel;

	protected FormPanel<MyFormattedTextField> numerFakturyPanel;

	protected CenaFormatter cenaFormatter = new CenaFormatter();

	protected IloscPoczFormatter iloscPoczFormatter = new IloscPoczFormatter(cenaFormatter);


	public PartiaAddPanelBasic(DBManager manager) {
		super(new MigLayout("fillx, wrap 2", "[min:50%:max, fill|min:50%:max, fill]"));
		this.manager = manager;
		produktNamePanel = factory.decotrateJTextField("Nazwa produktu", "produkt.nazwa");
		produktNamePanel.getFormElement().setEditable(false);
		jednostkaPanel = factory.decotrateComboBox("Jednostka", "jednostka", new AutocompleteComboBox(new JednostkaAdaptor(manager)));
		specyfikatorPanel = factory.decotrateComboBox("Specyfikatos", "specyfikator", new AutocompleteComboBox(new SpecyfikatorAdaptor(manager)));
		iloscPoczatkowaPanel = factory.decorateFormattedTextField("Ilość początkowa", "iloscPocz", new MyFormattedTextField(iloscPoczFormatter));
		iloscPoczatkowaPanel.getFormElement().setReadNullValues(false);
		cenaPanel = factory.decorateFormattedTextField("Cena:", "cena", new MyFormattedTextField(cenaFormatter));
		cenaPanel.getFormElement().setReadNullValues(false);
		dataWaznosciPanel = factory.decorateFormattedTextField("Data ważności", "dataWaznosci", new DataWaznosciFormatter());
		dataWaznosciPanel.getFormElement().setReadNullValues(true);
		dataksiegowaniaPanel = factory.decorateFormattedTextField("Data księgowania", "dataKsiegowania", new DateFormatter());
		numerFakturyPanel = factory.decorateFormattedTextField("Numer faktury", "numerFaktury", new NotEmptyFormatter("Numer faktury nie może być pusty"));
		form = factory.getCreatedForm();
		form.setDao(new PartiaDAO(manager));
		initGUI();
	}

	protected void initGUI() {
		add(produktNamePanel, "");
		add(jednostkaPanel, "");
		add(iloscPoczatkowaPanel);
		add(cenaPanel, "");
		add(numerFakturyPanel, "span 2");
		add(dataWaznosciPanel);
		add(dataksiegowaniaPanel);
		add(specyfikatorPanel, "");
	}

	public void setPartia(Partia partia){
		if(partia.getProdukt()==null){
			throw new IllegalArgumentException();
		}
		form.setEntity(partia);
	}

	public DAOForm<Partia, ? extends DAOFormElement> getForm() {
		return form;
	}

	public void clear(){
		jednostkaPanel.getFormElement().clear();
		iloscPoczatkowaPanel.getFormElement().clear();
		cenaPanel.getFormElement().clear();
		dataWaznosciPanel.getFormElement().clear();
		specyfikatorPanel.getFormElement().clear();
		numerFakturyPanel.getFormElement().clear();
	}


}
