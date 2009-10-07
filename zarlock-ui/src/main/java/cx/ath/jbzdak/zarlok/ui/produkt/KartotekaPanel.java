package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import static cx.ath.jbzdak.jpaGui.Utils.initLocation;
import static cx.ath.jbzdak.jpaGui.Utils.isIdNull;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.genericListeners.HideWindowActionListener;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.zarlok.db.dao.ProduktDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import cx.ath.jbzdak.zarlok.ui.partia.PartiaSelectPanel;
import cx.ath.jbzdak.zarlok.ui.wyprowadzenie.WyprowadzenieAddDialog;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KartotekaPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	ProduktDAO produktDAO;

	DBManager manager; //Do wystawienia dialogów do dodania partii i rozchod;

	JTable kartotekaTable;

	List<ElementKartoteki> kartoteka = Collections.emptyList();

	JButton addWyprowadzenieButton;

	JPanel buttonPanel = new JPanel();

	PartiaSelectPanel partiaSelectPanel = new PartiaSelectPanel();

	JDialog selectDialog;

	boolean userSelectedPartia = false;

	WyprowadzenieAddDialog wyprowadzenieAddDialog;

	public KartotekaPanel(ProduktDAO produktDAO, DBManager manager) {
		super(new BorderLayout());
		this.produktDAO = produktDAO;
		this.manager = manager;
		kartotekaTable = new JTable();
		initBindings();
		kartotekaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		for(int ii = 0 ; ii < kartotekaTable.getColumnModel().getColumnCount(); ii++){
			kartotekaTable.getColumnModel().getColumn(ii).setCellRenderer(new Renderer());
		}
		buttonPanel = new JPanel(new MigLayout());
		addWyprowadzenieButton = new JButton("Dodaj wyprowadzenie", IconManager.getScaled("wyprowadzenie", 1.25));
		addWyprowadzenieButton.setEnabled(false);
		buttonPanel.add(addWyprowadzenieButton);
		add(new JScrollPane(kartotekaTable), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		initEvts();
	}

	private void initBindings(){
		JTableBinding<ElementKartoteki, KartotekaPanel,JTable> binding = SwingBindings.createJTableBinding(
				UpdateStrategy.READ,
				this, BeanProperty.<KartotekaPanel, List<ElementKartoteki>>create("kartoteka"), kartotekaTable);
		binding.addColumnBinding(BeanProperty.<ElementKartoteki, Object>create("nazwa")).setColumnName("Nazwa");
		binding.addColumnBinding(BeanProperty.<ElementKartoteki, Object>create("cena")).setColumnName("Cena jednostkowa");
		binding.addColumnBinding(BeanProperty.<ElementKartoteki, Object>create("ilosc")).setColumnName("Ilość");
		binding.addColumnBinding(BeanProperty.<ElementKartoteki, Object>create("wartosc")).setColumnName("Wartość");
		binding.addColumnBinding(BeanProperty.<ElementKartoteki, Object>create("tytulem")).setColumnName("Tytułem");
		binding.bind();
	}


	private void initEvts(){
		addWyprowadzenieButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = kartotekaTable.getSelectedRow();
				Partia p;
				if(selectedIndex==-1){
					partiaSelectPanel.setModelList(produktDAO.getEntity().getPartie());
					getSelectDialog().setVisible(true);
					if(!userSelectedPartia){
						return;
					}
					p = partiaSelectPanel.getSelectedPartia();
				}else{
					Object selected = kartoteka.get(selectedIndex).getSourceObject();
					if (selected instanceof Partia) {
						p = (Partia) selected;
					}else if (selected instanceof Wyprowadzenie) {
						p = ((Wyprowadzenie) selected).getPartia();
					}else{
						throw new IllegalStateException();
					}
				}
				getWyprowadzenieAddDialog().showDialog(p);
			}
		});
	}

	private void refreshKartoteka(){
		produktDAO.beginTransaction();
		try{
			List<ElementKartoteki> karoteka = new ArrayList<ElementKartoteki>();
			for(Partia p : produktDAO.getEntity().getPartie()){
				karoteka.add(new ElementKartoteki(p));
				for(Wyprowadzenie w : p.getWyprowadzenia()){
					karoteka.add(new ElementKartoteki(w));
				}
			}
			setKartoteka(karoteka);
		}finally{
			produktDAO.closeTransaction();
		}
	}

	public void clearEntity() {
		produktDAO.clearEntity();
	}


	public void createEntity() {
		produktDAO.createEntity();
	}


	public Produkt getEntity() {
		return produktDAO.getEntity();
	}


	public void setEntity(Produkt entity) {
		produktDAO.setEntity(entity);
		 refreshKartoteka();
		 addWyprowadzenieButton.setEnabled(entity!= null && !isIdNull(entity));

	}

	public List<ElementKartoteki> getKartoteka() {
		return kartoteka;
	}

	public void setKartoteka(List<ElementKartoteki> kartoteka) {
		if(this.kartoteka == null || !this.kartoteka.equals(kartoteka)){
			List<ElementKartoteki> oldKartoteka = this.kartoteka;
			this.kartoteka = kartoteka;
			firePropertyChange("kartoteka", oldKartoteka, kartoteka);
		}
	}

	private JDialog getSelectDialog(){
		if(selectDialog == null){
			selectDialog = new JDialog(SwingUtilities.getWindowAncestor(this));
			selectDialog.setLayout(new BorderLayout());
			selectDialog.add(partiaSelectPanel, BorderLayout.CENTER);
			partiaSelectPanel.getOkButton().addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					selectDialog.setVisible(false);
					userSelectedPartia=true;
				}
			});
			selectDialog.setModal(true);
			selectDialog.pack();
			initLocation(selectDialog);
			partiaSelectPanel.getCancelButton().addActionListener(new HideWindowActionListener(selectDialog));
		}
		return selectDialog;
	}

	public WyprowadzenieAddDialog getWyprowadzenieAddDialog() {
		if(wyprowadzenieAddDialog==null){
			wyprowadzenieAddDialog = new WyprowadzenieAddDialog(SwingUtilities.getWindowAncestor(this), manager);
			wyprowadzenieAddDialog.getButtonPanel().setCommitOnOK(false);
			wyprowadzenieAddDialog.getButtonPanel().getOkTasks().addTask(new Task<DAOForm>(){
				@Override
				public void doTask(DAOForm t, Object... o)
						throws Exception {
					refreshKartoteka();
				}
			});
			wyprowadzenieAddDialog.getButtonPanel().addBothTaskAction(new Task<DAOForm>(){

				@Override
				public void doTask(DAOForm t, Object... o)
						throws Exception {
					wyprowadzenieAddDialog.hideDialog();
				}

			});
		}
		return wyprowadzenieAddDialog;
	}

	private class Renderer extends DefaultTableCellRenderer{

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cmp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			Color bckgrount = Color.GREEN;
			if(kartoteka.get(row).sourceObject instanceof Wyprowadzenie){
				bckgrount = Color.PINK;
			}
			cmp.setBackground(bckgrount);
			return cmp;
		}

	}

	public JButton getAddWyprowadzenieButton() {
		return addWyprowadzenieButton;
	}


}
