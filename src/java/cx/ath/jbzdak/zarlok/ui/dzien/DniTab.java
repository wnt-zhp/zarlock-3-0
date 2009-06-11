package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.db.dao.DzienDao;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import javax.persistence.EntityManager;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import java.util.Collection;

public class DniTab extends JSplitPane{

	private static final long serialVersionUID = 1L;

	private final DBManager manager;

	private final DzienDao sidebarDao;

	private final DniTreePanel dniTreePanel;

	private EntityManager entityManager;

	public DniTab(MainWindowModel mainWindowModel) {
		setOrientation(HORIZONTAL_SPLIT);
		this.manager = mainWindowModel.getManager();
		this.sidebarDao = new DzienDao(manager);
		dniTreePanel = new DniTreePanel(sidebarDao, mainWindowModel);
		setRightComponent(new JScrollPane(dniTreePanel.getDetailsPanel()));
		setLeftComponent(dniTreePanel);
	}

	public void setDni(Collection<Dzien> dni) {
		dniTreePanel.setDni(dni);
	}

	@SuppressWarnings("unchecked")
	public void tabVisible(){
		entityManager = manager.createEntityManager();
		sidebarDao.setEntityManager(entityManager);
		dniTreePanel.setEntityManager(entityManager);
      dniTreePanel.setDni(entityManager.createQuery("SELECT d FROM Dzien d").getResultList());
	}

	public void tabHidden(){
		entityManager.close();
	}




}
