package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.zarlok.db.dao.ProduktDAO;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.entities.TakieSamePartie;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class StanPanel extends JPanel{

private static final long serialVersionUID = 1L;


	ProduktDAO produktDAO;

	JTable table;

	@SuppressWarnings("unchecked")
	JTableBinding  binding;

	List<TakieSamePartie> stanList = Collections.emptyList();

	public StanPanel(ProduktDAO produktDAO) {
		super(new BorderLayout());
		this.produktDAO = produktDAO;
		table = new JTable();
		initBinding();
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	@SuppressWarnings("unchecked")
	private void initBinding(){
		binding = SwingBindings.createJTableBinding(UpdateStrategy.READ, this,
				BeanProperty.<StanPanel, List<TakieSamePartie>>create("stanList"), table);
		binding.addColumnBinding(BeanProperty.create("produkt.nazwa")).setColumnName("Nazwa");
		binding.addColumnBinding(BeanProperty.create("specyfikator")).setColumnName("Specyfikator");
		binding.addColumnBinding(BeanProperty.create("jednostka")).setColumnName("Jednostka");
		binding.addColumnBinding(BeanProperty.create("iloscTeraz")).setColumnName("Ilość");
		binding.bind();
	}

	@SuppressWarnings("unchecked")
	private void fetchList(){
		produktDAO.beginTransaction();
		try{
			EntityManager manager = produktDAO.getEntityManager();
			Query q = manager.createNamedQuery("getPartieByProdukt");
			q.setParameter("produkt", produktDAO.getEntity());
			setStanList(q.getResultList());
		}finally{
			produktDAO.closeTransaction();
		}
	}

	public void clearEntity() {
		produktDAO.clearEntity();
	}


	public void createEntity() {
		produktDAO.createEntity();
		refresh();
	}


	public Produkt getEntity() {
		return produktDAO.getEntity();
	}


	public void setEntity(Produkt entity) {
		produktDAO.setEntity(entity);
		refresh();
	}

	public void refresh(){
		fetchList();
	}

	public List<TakieSamePartie> getStanList() {
		return stanList;
	}

	public void setStanList(List<TakieSamePartie> stanList) {
		if(this.stanList==null || !this.stanList.equals(stanList)){
			List<TakieSamePartie> partie = this.stanList;
			this.stanList = stanList;
			firePropertyChange("stanList", partie, this.stanList);
		}
	}

}
