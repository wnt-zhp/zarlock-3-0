package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.Factory;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.*;

import javax.swing.*;
import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.collections.map.DefaultedMap;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.Transformer;

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
         if(ICON_MANAGER.getIcon(getString(buttonLabel))!=null){
            return new JButton(getString(buttonLabel), ICON_MANAGER.getIcon(getString(buttonLabel)));
         }
         return new JButton(getString(buttonLabel));
      }
   }
   
}
