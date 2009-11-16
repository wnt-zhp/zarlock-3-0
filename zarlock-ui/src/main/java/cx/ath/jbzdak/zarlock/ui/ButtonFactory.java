package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.Factory;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ButtonFactory {

   private static Map<String, Factory<JButton>> buttons = LazyMap.decorate(new HashMap(), new Transformer() {
      @Override
      public Object transform(Object input) {
         return new ButtonFactoryInternal((String) input,null);
      }
   });

   public static JButton createButton(String name){
      return buttons.get(name).make();
   }


   private static class ButtonFactoryInternal implements Factory<JButton>{

      @CheckForNull
      private final String iconName;

      @Nonnull
      private final String buttonLabel;

      private ButtonFactoryInternal(@Nonnull String buttonLabel, @CheckForNull String iconName) {
         this.iconName = iconName;
         this.buttonLabel = buttonLabel;
      }

      @Override
      public JButton make() {
         if(iconName!= null){
            return new JButton(getString(buttonLabel), ICON_MANAGER.getIcon(iconName));
         }
         if(ICON_MANAGER.getIcon(buttonLabel)!=null){
            return new JButton(getString(buttonLabel), ICON_MANAGER.getIcon(buttonLabel));
         }
         return new JButton(getString(buttonLabel));
      }
   }
   
}
