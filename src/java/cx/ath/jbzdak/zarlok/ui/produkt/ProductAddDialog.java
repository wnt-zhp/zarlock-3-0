package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.OkButtonFormPanel;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

public class ProductAddDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private DBManager DBManager = null;  //  @jve:decl-index=0:visual-constraint="551,155"
	private OkButtonFormPanel<Produkt> okButtonPanel = null;
	private ProductAddPanel productAddPanel1 = null;

	/**
	 * @param owner
	 */
	public ProductAddDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		Point location = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		pack();
		location.x-= getWidth()/2;
		location.y -= getHeight()/2;
		setLocation(location);
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getOkButtonPanel(), BorderLayout.SOUTH);
			jContentPane.add(getProductAddPanel1(), BorderLayout.CENTER);
		}
		return jContentPane;
	}


	/**
	 * This method initializes okButtonPanel
	 *
	 * @return cx.ath.jbzdak.zarlok.ui.wrapper.OkButtonPanel
	 */
	private OkButtonFormPanel<Produkt> getOkButtonPanel() {
		if (okButtonPanel == null) {
			okButtonPanel = new OkButtonFormPanel<Produkt>();
			Task<DAOForm<? extends Produkt, ?>> hideTask = new Task<DAOForm<? extends Produkt,?>>(100, "HIDE"){
				@Override
				public void doTask(DAOForm<? extends Produkt,?> t, Object... o)
						throws Exception {
					ProductAddDialog.this.setVisible(false);
				}

			};
			okButtonPanel.getOkTasks().addTask(hideTask);
			okButtonPanel.getCancelTasks().addTask(hideTask);
		}
		return okButtonPanel;
	}

	/**
	 * This method initializes productAddPanel1
	 *
	 * @return cx.ath.jbzdak.zarlok.ui.produkt.ProductAddPanel
	 */
	private ProductAddPanel getProductAddPanel1() {
		if (productAddPanel1 == null) {
			productAddPanel1 = new ProductAddPanel();
		}
		return productAddPanel1;
	}

	public void setDBManager(DBManager manager) {
		if(DBManager!=null){
			throw new IllegalStateException();
		}
		DBManager = manager;
		getProductAddPanel1().setManager(manager);
		getProductAddPanel1().initialize();
		validate();
		pack();
		okButtonPanel.setForm(productAddPanel1.getForm());
	}

	public void commit() {
		productAddPanel1.commit();
	}

	public void setProdukt(Produkt p) {
		productAddPanel1.setProdukt(p);
	}

	public void startEditing() {
		productAddPanel1.startEditing();
	}

	public void startViewing() {
		productAddPanel1.startViewing();
	}

	public void stopEditing() {
		productAddPanel1.stopEditing();
	}

}
