package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.zarlok.entities.TakieSamePartie;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;

import java.util.List;

public class StanMagazynuPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	StanMagazynuTable partieTable;

	StanMagazynuFilterPanel filter;

	public StanMagazynuPanel(){
		super(new MigLayout("fillx", "[grow]", "[grow]"));
		partieTable = new StanMagazynuTable();
		filter = new StanMagazynuFilterPanel(partieTable);
		add(filter, "dock north, grow");
		add(new JScrollPane(partieTable), "grow");
	}

	public List<TakieSamePartie> getContents() {
		return partieTable.getContents();
	}

	public void setContents(List<TakieSamePartie> contents) {
		partieTable.setContents(contents);
	}



}
