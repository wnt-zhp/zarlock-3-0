package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.task.Task;
import javax.annotation.Nullable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-04
 */
public class FetcherDebug extends Task<Object>{

   private Timer timer;

   private final Vector waitList;

   private static final Logger LOGGER = Utils.makeLogger();
   {
      Vector w = new Vector();
      try {
         Class<?> fetcherInfoClass = Class.forName("sun.awt.image.FetcherInfo");
         Method method = fetcherInfoClass.getDeclaredMethod("getFetcherInfo");
         method.setAccessible(true);
         Object fetcher = method.invoke(null);
         Field waitListField = fetcherInfoClass.getDeclaredField("waitList");
//         for(Field f : fetcherInfoClass.getDeclaredFields()){
//
//         }
         waitListField.setAccessible(true);
         w = (Vector) waitListField.get(fetcher);
      } catch (Exception e) {
         e.printStackTrace();
      }finally {
         waitList = w;
      }
   }

   public FetcherDebug() {
      super(100, "FetcherDebug ");
   }

   @Override
   public void doTask(@Nullable Object o2, @Nullable Object... o) throws Exception {
      timer = new Timer("IMAGE-FETCHER-WATCHER", true);
      timer.scheduleAtFixedRate(new TimerTask() {
         @Override
         public void run() {
            //LOGGER.info("FetcherDebug.run");
            synchronized (waitList){
                     if(waitList.size()==0){
                        //LOGGER.info("FetcherInfo.waitList is empty");
                     }else{
                        LOGGER.info("FetcherInfo.waitList contains {} elements", waitList.size());
                        for(Object o : waitList){
                           LOGGER.info("XXXXX" + ToStringBuilder.reflectionToString(o));
                        }
                     }
            }
         }
      }, 100, 50);
   }


}
