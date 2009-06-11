package cx.ath.jbzdak.zarlok.ui.iloscOsob;

import static cx.ath.jbzdak.jpaGui.Utils.initLocation;
import cx.ath.jbzdak.jpaGui.ui.form.OkButtonFormPanel;
import cx.ath.jbzdak.zarlok.entities.IloscOsob;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

import java.awt.Window;

public class IloscOsobDialog extends JDialog{


	private static final long serialVersionUID = 1L;

	private final IloscOsobPanel panel;

	private final OkButtonFormPanel<IloscOsob> buttonPanel;

	public IloscOsobDialog(){
		this(null);
	}

	public IloscOsobDialog(Window owner) {
		super(owner, "Ustaw ilość osób");
		setLayout(new MigLayout());
		panel = new IloscOsobPanel();
		buttonPanel = new OkButtonFormPanel<IloscOsob>();
		buttonPanel.setForm(panel.getForm());
		add(panel);
		add(buttonPanel, "south");
		pack();
		initLocation(this);
	}

	public void showDialog(IloscOsob i){
		panel.setEntity(i);
		panel.startEditing();
		setVisible(true);
	}

	public void hideDialog(){
		panel.commit();
		panel.setEntity(null);
		setVisible(false);
	}

	public OkButtonFormPanel<IloscOsob> getButtonPanel() {
		return buttonPanel;
	}
}