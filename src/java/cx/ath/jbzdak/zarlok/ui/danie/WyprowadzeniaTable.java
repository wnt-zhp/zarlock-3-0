package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.table.HighlightCellRenderer;
import cx.ath.jbzdak.jpaGui.ui.table.TableObjectProperty;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import java.util.List;


public class WyprowadzeniaTable extends JTable {

	private static final long serialVersionUID = 1L;

	WyprowadzenieTableModel wyprowadzeniaModel;

	@SuppressWarnings("unchecked")
	JTableBinding wyprowadzoneBinding;

	public WyprowadzeniaTable(DBManager manager){
		wyprowadzeniaModel = new WyprowadzenieTableModel(manager, this);
        wyprowadzeniaModel.setInsertNewRow(false);
		setRowHeight(25);
		initBinding();
		TableColumnModel columnModel = getColumnModel();
		TableColumn nazwaPartiiColumn = columnModel.getColumn(0);
		nazwaPartiiColumn.setCellRenderer(new WyprowadzoneRenderer());
		AutocompleteComboBox autocompleteComboBox = new AutocompleteComboBox(
				new SelectPartiaAdaptor(manager));
		autocompleteComboBox.setRenderer(new PartiaSelectRenderer());
		autocompleteComboBox.setStrict(true);
		nazwaPartiiColumn.setCellEditor(new DefaultCellEditor(
				autocompleteComboBox));
		TableColumn iloscColumn = columnModel.getColumn(1);
		iloscColumn.setCellRenderer(new WyprowadzoneRenderer());
		TableColumn akcjeColumn = columnModel.getColumn(2);
		akcjeColumn.setCellRenderer(wyprowadzeniaModel.createRendererEditor());
		akcjeColumn.setCellEditor(wyprowadzeniaModel.createRendererEditor());
		iloscColumn.setMaxWidth(75);
		akcjeColumn.setMaxWidth(90);
	}


	@SuppressWarnings("unchecked")
	private void initBinding(){
		wyprowadzoneBinding = SwingBindings.createJTableBinding(
				UpdateStrategy.READ, wyprowadzeniaModel,
				BeanProperty.<WyprowadzenieTableModel, List<Wyprowadzenie>>create("entities"), this);
		wyprowadzoneBinding.addColumnBinding(BeanProperty.create("partia"))
				.setColumnName("Nazwa").setEditable(false).setConverter(
						new Cnvtr());
		wyprowadzoneBinding.addColumnBinding(
				BeanProperty.create("iloscJednostek")).setColumnName("Ilość")
				.setEditable(false);
		wyprowadzoneBinding.addColumnBinding(new TableObjectProperty())
				.setColumnName("Akcje").setEditable(true);
		wyprowadzoneBinding.bind();
	}

	private static class Cnvtr extends Converter<Object, Object> {

		@Override
		public Object convertForward(Object value) {
			return value;
		}

		@Override
		public Object convertReverse(Object value) {
			Partia p;
			if (value instanceof AutoCompleteValueHolder) {
				AutoCompleteValueHolder acvh = (AutoCompleteValueHolder) value;
				if(!(acvh.getValue() instanceof Partia)){
					throw new IllegalArgumentException("'" + value.toString() + "' is not instanceof Partia");
				}
				p = (Partia) acvh.getValue();
			} else {
				if(!(value instanceof Partia)){
					throw new IllegalArgumentException("'" + value.toString() + "' is not instanceof Partia");
				}
				p = (Partia) value;
			}
         return p;

		}

	}
	public class WyprowadzoneRenderer extends HighlightCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean isHighlighted(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			return wyprowadzeniaModel.isHighlighted(row) && !isSelected;
		}

		@Override
		protected void getTableCellRendererEntry(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			setText(value==null?"":value.toString());
		}
	}

	public Danie getDanie() {
		return wyprowadzeniaModel.getDanie();
	}

	public void setDanie(Danie danie) {
		wyprowadzeniaModel.setDanie(danie);
	}

}
