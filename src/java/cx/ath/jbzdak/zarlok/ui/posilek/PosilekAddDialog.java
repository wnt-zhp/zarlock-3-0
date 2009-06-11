package cx.ath.jbzdak.zarlok.ui.posilek;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.genericListeners.HideWindowTask;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.OkButtonFormPanel;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

import java.awt.Window;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-02
 */
public class PosilekAddDialog extends JDialog{

   private final PosilekAddPanel posilekAddPanel;

   private final OkButtonFormPanel buttonFormPanel;

   public PosilekAddDialog(Window owner, DBManager manager) {
      super(owner);
      setModal(true);
      setLayout(new MigLayout("wrap 1"));
      posilekAddPanel = new PosilekAddPanel(manager);
      buttonFormPanel = new OkButtonFormPanel();
      add(posilekAddPanel);
      add(buttonFormPanel);
      pack();
      buttonFormPanel.setCommitOnOK(false);
      buttonFormPanel.addBothTaskAction(new HideWindowTask<DAOForm>(this));
   }

   public Posilek getPosilek() {return posilekAddPanel.getPosilek();}

   public void setPosilek(Posilek posilek) {posilekAddPanel.setPosilek(posilek);}

   public void addOkActionTask(Task task) {buttonFormPanel.addOkActionTask(task);}

   public void addCancelActionTask(Task task) {buttonFormPanel.addCancelActionTask(task);}

   public void addBothTaskAction(Task task) {buttonFormPanel.addBothTaskAction(task);}
}
