package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.db.dao.JPADao;
import cx.ath.jbzdak.zarlok.entities.Dzien;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.util.Collection;

public class DniTab extends JSplitPane{

	private static final long serialVersionUID = 1L;

	private final DniTreePanel dniTreePanel;

	public DniTab() {
		setOrientation(HORIZONTAL_SPLIT);
		dniTreePanel = new DniTreePanel();
		setRightComponent(new JScrollPane(dniTreePanel.getDetailsPanel()));
		setLeftComponent(dniTreePanel);
	}

	public void setDni(Collection<Dzien> dni) {
		dniTreePanel.setDni(dni);
	}



}
