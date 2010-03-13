package cx.ath.jbzdak.zarlock.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-16
 */
public class ZarlockUtils {

   public static ZarlockModel getZarlockModel(Component component){
      if(component instanceof Window){
         return getModelNotRecursive((Window)component);
      }
      return getModelRecursive(component);
   }

   private static ZarlockModel getModelRecursive(Component component){
      Window window = SwingUtilities.getWindowAncestor(component);
      return getModelNotRecursive(window);
   }

   private static ZarlockModel getModelNotRecursive(Window window){
      if(window==null){
         return null;
      }
      if (window instanceof ZarlockFrame) {
         ZarlockFrame zarlockFrame = (ZarlockFrame) window;
         return zarlockFrame.getZarlockModel();
      }
      ZarlockModel zarlockModel = getModelRecursive(window);
      return zarlockModel!=null?zarlockModel:getModelNotRecursive(window.getOwner());
   }



}
