package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.jpaGui.ui.table.AutoCompleteComboBoxEditor;
import cx.ath.jbzdak.jpaGui.ui.table.TableObjectProperty;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.JednostkaAdaptor;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.PartiaAdaptor;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.SpecyfikatorAdaptor;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import java.util.List;

public class PlanowaneTable extends JTable{

	private static final long serialVersionUID = 1L;

	private final DBManager manager;

	@SuppressWarnings("unchecked")
	JTableBinding planowaneBinding;

	PlanowaneTableModel planowaneModel;

	public PlanowaneTable(DBManager manager) {
		super();
		this.manager = manager;
		planowaneModel = new PlanowaneTableModel(manager, this);
		setRowHeight(25);
		initBinding();
		{//ComboBox z nazwą produktu
			TableColumn productNameColumn = getColumnModel().getColumn(0);
			AutocompleteComboBox productNameComboBox = new AutocompleteComboBox(new PartiaAdaptor(manager));
			productNameComboBox.setStrict(true);
			productNameColumn.setCellEditor(new AutoCompleteComboBoxEditor(productNameComboBox));
		}
		{//ComboBox ze specyfikatorem
			TableColumn specifierColumn = getColumnModel().getColumn(1);
			AutocompleteComboBox specifierBox = new AutocompleteComboBox(new SpecyfikatorAdaptor(manager));
			specifierColumn.setCellEditor(new AutoCompleteComboBoxEditor(specifierBox));
		}
		{
			TableColumn jednostkaColumn = getColumnModel().getColumn(2);
			AutocompleteComboBox jednostka = new AutocompleteComboBox(new JednostkaAdaptor(manager));
			jednostkaColumn.setCellEditor(new AutoCompleteComboBoxEditor(jednostka));
		}
		{
			TableColumn akcjeColumn = getColumnModel().getColumn(4);
			akcjeColumn.setCellEditor(planowaneModel.createRendererEditor());
			akcjeColumn.setCellRenderer(planowaneModel.createRendererEditor());
		}
		setDefaultRenderer(Object.class, planowaneModel.createHighlightRenderer());
	}


	@SuppressWarnings("unchecked")
	private void initBinding(){
		planowaneBinding = SwingBindings.createJTableBinding(
				UpdateStrategy.READ, planowaneModel,
				BeanProperty.<PlanowaneTableModel, List<PlanowaneTableModel>>create("entities"), this);
		planowaneBinding.addColumnBinding(BeanProperty.create("contents"))
				.setColumnName("Nazwa produktu").setEditable(true).setConverter(
						new WyprowadzenieConverter());
		planowaneBinding.addColumnBinding(BeanProperty.create("specyfikator"))
			.setColumnName("Specyfikator").setEditable(true);
		planowaneBinding.addColumnBinding(BeanProperty.create("jednostka"))
			.setColumnName("Jednostka").setEditable(true);
		planowaneBinding.addColumnBinding(
				BeanProperty.create("iloscJednostek")).setColumnName("Ilość")
				.setEditable(true);
		planowaneBinding.addColumnBinding(new TableObjectProperty())
				.setColumnName("Akcje").setEditable(true);
		planowaneBinding.bind();

	}


	private class WyprowadzenieConverter extends Converter<Object, Object>{

		@Override
		public Object convertForward(Object value) {
			ProductSearchCache cache = (ProductSearchCache) value;
			if(cache!=null && cache.getProduct()!=null){
				return cache.getProduct().getNazwa();
			}
			return "";
		}

		@Override
		public Object convertReverse(Object value) {
			if (value instanceof AutoCompleteValueHolder) {
				value = ((AutoCompleteValueHolder) value).getValue();
			}
			final ProductSearchCache cache = (ProductSearchCache) value;
			Transaction.execute(manager, new Transaction(){
				@Override
				public void doTransaction(EntityManager entityManager) {
					cache.setProduct(entityManager.find(Produkt.class, cache.getProductId()));
				}
			});
			return value;
		}

	}


	public Danie getDanie() {
		return planowaneModel.getDanie();
	}


	public void setDanie(final Danie danie) {

		planowaneModel.setDanie(danie);
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getType()==TableModelEvent.UPDATE && (e.getColumn()==TableModelEvent.ALL_COLUMNS || e.getColumn()==0)){
			if(planowaneBinding!=null && planowaneBinding.isBound())
				planowaneBinding.refresh();
		}
		super.tableChanged(e);
	}
}
