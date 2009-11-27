package cx.ath.jbzdak.zarlock.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-16
 */
public class ZarlockUtils {

   public static ZarlockModel getZarlockModel(Component component){
      Window window = SwingUtilities.getWindowAncestor(component);
      if(window==null){
         return null;
      }
      if (window instanceof ZarlockFrame) {
         ZarlockFrame zarlockFrame = (ZarlockFrame) window;
         return zarlockFrame.getZarlockModel();
      }
      return getZarlockModel(window);
   }

}
