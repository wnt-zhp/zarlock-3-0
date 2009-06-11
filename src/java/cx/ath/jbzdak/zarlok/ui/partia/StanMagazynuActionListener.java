package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import javax.persistence.EntityManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StanMagazynuActionListener implements ActionListener {

	private final DBManager manager;

	private final StanMagazynuPanel panel;


	public StanMagazynuActionListener() {
		super();
		manager = null;
		panel = null;
	}


	public StanMagazynuActionListener(DBManager manager, StanMagazynuPanel panel) {
		super();
		this.manager = manager;
		this.panel = panel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if("SELECTED".equals(e.getActionCommand())){
			EntityManager em = manager.createEntityManager();
			try{
				panel.setContents(em.createNamedQuery("getPartie").getResultList());
			}finally{
				em.close();
			}
		}
	}

}
