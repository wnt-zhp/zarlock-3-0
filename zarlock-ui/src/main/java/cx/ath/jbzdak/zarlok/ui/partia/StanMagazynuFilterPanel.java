package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.ui.query.FulltextJTtextField;
import cx.ath.jbzdak.zarlok.ui.query.StanMagazynuFilter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

public class StanMagazynuFilterPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private ModelListener modelListener = new ModelListener();

	private JTable table;

	private TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>();

	private StanMagazynuFilter<TableModel, Integer> stanMagazynuFilter = new StanMagazynuFilter<TableModel, Integer>();

	private FulltextJTtextField textField = new FulltextJTtextField(stanMagazynuFilter.getFulltextQuery());

	private JCheckBox fuzzyBox = new JCheckBox();

	private JCheckBox jestNaSkladzieBox = new JCheckBox();

	public StanMagazynuFilterPanel(JTable table) {
		super(new MigLayout("fillx", "[fill|min!|min!]"));
		setTable(table);
		add(new JLabel("Zapytanie: "));
		add(new JLabel("Wyszukiwanie rozmyte: "));
		add(fuzzyBox);
		add(textField, "newline");
		add(new JLabel("Jest w magazynie:"), "");
		add(jestNaSkladzieBox);
		jestNaSkladzieBox.setSelected(true);
		initBindings();
	}

	private void initBindings(){
		textField.addQueryChangedListener(new QueryListener());
		fuzzyBox.addItemListener(new CheckboxesListener());
		jestNaSkladzieBox.addItemListener(new CheckboxesListener());
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		if(this.table!=null){
			this.table.removePropertyChangeListener(modelListener);
			this.table.setRowSorter(null);
		}
		this.table = table;
		this.table.addPropertyChangeListener(new ModelListener());
		installFilter();
	}

	private void installFilter(){
		rowSorter.setModel(table.getModel());
		rowSorter.setRowFilter(stanMagazynuFilter);
		table.setRowSorter(rowSorter);
	}

	private class ModelListener implements PropertyChangeListener{
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if("model".equals(evt.getPropertyName())){
				installFilter();
			}
		}
	}

	private class QueryListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			rowSorter.sort();
		}
	}

	private class CheckboxesListener implements ItemListener, Serializable{

		private static final long serialVersionUID = 1L;

		@Override
		public void itemStateChanged(ItemEvent e) {
			stanMagazynuFilter.setJestNaSkladzie(jestNaSkladzieBox.isSelected());
			stanMagazynuFilter.setFuzzy(fuzzyBox.isSelected());
			rowSorter.sort();

		}

	}
}
