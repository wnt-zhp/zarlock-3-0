package cx.ath.jbzdak.zarlock.ui.product;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-13
 */
public class ProductTab extends JPanel{

   private JPanel productSelectPanel; 

   public ProductTab() {
      setLayout(new MigLayout("fillx, filly", "[grow][grow]", "[][grow]"));
      
   }

   
}
