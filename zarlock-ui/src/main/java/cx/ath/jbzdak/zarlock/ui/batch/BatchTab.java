package cx.ath.jbzdak.zarlock.ui.batch;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.List;

import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 14, 2010
 */
public class BatchTab extends JPanel{

   BatchList batchList = new BatchList();

   public BatchTab() {
      super(new BorderLayout());
      add(batchList, BorderLayout.CENTER);
   }

   public void setBathes(List<Batch> bathes) {
      batchList.setBathes(bathes);
   }
}
