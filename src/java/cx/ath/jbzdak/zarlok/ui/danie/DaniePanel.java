package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.table.TablePanel;
import cx.ath.jbzdak.zarlok.entities.Danie;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingbinding.JTableBinding;

import javax.persistence.EntityManager;
import javax.swing.*;

public class DaniePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	DBManager manager;

	EntityManager entityManager;


	// Komponenty niezależne od dania:

	JLabel wydanoLabel, planowanoLabel;

	{
		wydanoLabel = new JLabel("<html><strong>Wydano:</strong></html>");
		planowanoLabel = new JLabel("<html><strong>Planowano:</strong></html>");
	}

	PatternBeanFormatter nazwaDaniaFormatter = new PatternBeanFormatter(
			"<html><font size=\"+1\"><strong>{nazwa}</strong>:</font> "
					+ "({#0}{(costStrict)?\"\":\"<i>\"}{koszt}{#0}{(costStrict)?\"\":\"</i>\"}zł)</html>");

	// Komponenty zależne od dania:

	Danie danie;

	JLabel nazwaDania;

	@SuppressWarnings("unchecked")
	JTableBinding wyprowadzoneBinding;

	WyprowadzeniaTable wyprowadzeniaTable;

	PlanowaneTable planowaneTable;

   AddWyprowadzeniePanel addWyprowadzeniePanel;

	public DaniePanel(DBManager manager) {
		super(new MigLayout("wrap 2, fillx", "[15px!|fill]"));
		if (manager == null) {
			throw new IllegalArgumentException();
		}
		this.manager = manager;
		wyprowadzeniaTable = new WyprowadzeniaTable(manager);
		planowaneTable = new PlanowaneTable(manager);
		nazwaDania = new JLabel();
      addWyprowadzeniePanel = new AddWyprowadzeniePanel(manager, wyprowadzeniaTable);
		add(nazwaDania, "span 2");
		add(wydanoLabel, "skip 1");
		add(new TablePanel(wyprowadzeniaTable), "skip 1");
      add(addWyprowadzeniePanel, "skip 1");
		//add(planowanoLabel, "skip 1");
		//add(new TablePanel(planowaneTable), "skip 1");
	}

	private void initGui() {
		nazwaDania.setText(nazwaDaniaFormatter.format(danie));

            }

	public Danie getDanie() {
		return danie;
	}

	public void setDanie(Danie danie) {
		this.danie = danie;
		wyprowadzeniaTable.setDanie(danie);
		planowaneTable.setDanie(danie);
      addWyprowadzeniePanel.setDzien(danie.getPosilek().getDzien());
		initGui();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

   }
