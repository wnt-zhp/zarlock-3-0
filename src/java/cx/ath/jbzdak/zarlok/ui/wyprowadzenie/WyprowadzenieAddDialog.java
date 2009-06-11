package cx.ath.jbzdak.zarlok.ui.wyprowadzenie;

import static cx.ath.jbzdak.jpaGui.Utils.initLocation;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.OkButtonFormPanel;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

import java.awt.Window;

public class WyprowadzenieAddDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	private final WyprowadzenieAddPanel panel;

	private final OkButtonFormPanel<Wyprowadzenie> buttonPanel;

	public WyprowadzenieAddDialog(DBManager manager){
		this(null, manager);
	}


	public WyprowadzenieAddDialog(Window owner, DBManager manager) {
		super(owner, "Dodaj wyprowadzenie");
		setLayout(new MigLayout());
		panel = new WyprowadzenieAddPanel(manager);
		buttonPanel = new OkButtonFormPanel<Wyprowadzenie>();
		buttonPanel.setForm(panel.getForm());
		add(panel);
		add(buttonPanel, "south");
		pack();
		initLocation(this);
	}

	public void showDialog(Partia p){
		panel.setPartia(p);
		setVisible(true);
		panel.startEditing();
	}

	public void hideDialog(){
		panel.commit();
		panel.setPartia(null);
		setVisible(false);
	}

	public OkButtonFormPanel<Wyprowadzenie> getButtonPanel() {
		return buttonPanel;
	}


}
