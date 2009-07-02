package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.FormElement;
import cx.ath.jbzdak.jpaGui.ui.form.OkButtonFormPanel;
import cx.ath.jbzdak.zarlok.entities.Partia;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class PartiaAddDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private PartiaAddPanel partiaAddPanel = null;
	private final DBManager manager;
	private OkButtonFormPanel<Partia> okButtonPanel = null;

   private boolean completedSuccesfully = false;

	/**
	 * @param owner
	 * @deprecated uzywać {@link PartiaAddDialog#PartiaAddDialog(Frame, DBManager)}
	 */
	@Deprecated
	public PartiaAddDialog(Frame owner) {
		super(owner);
		initialize();
		manager = null;
	}

	public PartiaAddDialog(Frame owner, DBManager manager) {
		super(owner);
		this.manager = manager;
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
			jContentPane.add(getPartiaAddPanel(), BorderLayout.CENTER);
			jContentPane.add(getOkButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes partiaAddPanel
	 *
	 * @return cx.ath.jbzdak.zarlok.ui.partia.PartiaAddPanel
	 */
	private PartiaAddPanel getPartiaAddPanel() {
		if (partiaAddPanel == null) {
			partiaAddPanel = new PartiaAddPanel(manager);
		}
		return partiaAddPanel;
	}

	/**
	 * This method initializes okButtonPanel
	 *
	 * @return cx.ath.jbzdak.zarlok.ui.wrapper.OkButtonPanel
	 */
	private OkButtonFormPanel<Partia> getOkButtonPanel() {
		if (okButtonPanel == null) {
			okButtonPanel = new OkButtonFormPanel<Partia>();
			okButtonPanel.setForm(getPartiaAddPanel().getForm());
			okButtonPanel.addBothTaskAction(new Task<DAOForm>(1, ""){
				@Override
				public void doTask(DAOForm t, Object... o) throws Exception {
					setVisible(false);
				}
			});
         okButtonPanel.addOkActionTask(new Task<DAOForm>() {
            @Override
            public void doTask(@Nullable DAOForm daoForm, @Nullable Object... o) throws Exception {
               completedSuccesfully = true;
            }
         });
		}
		return okButtonPanel;
	}

   @Override
   public void setVisible(boolean b) {
      if(b){
         getPartiaAddPanel().reset();
      }
      super.setVisible(b);
   }

   public DAOForm<Partia, ? extends FormElement> getForm() {
		return partiaAddPanel.getForm();
	}

	public void clear() {
		partiaAddPanel.clear();
	}

   public boolean isCompletedSuccesfully() {
      return completedSuccesfully;
   }

   public void setCompletedSuccesfully(boolean completedSuccesfully) {
      this.completedSuccesfully = completedSuccesfully;
   }
}
