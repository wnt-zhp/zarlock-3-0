package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.zarlock.ui.batch.ProductSearchCacheAdaptor;
import cx.ath.jbzdak.zarlock.ui.ZarlockFrame;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
public class TestPSCAdaptor {

   public static void main(String[] args){
      AppLauncher appLauncher = new AppLauncher();
      appLauncher.start();
      ProductSearchCacheAdaptor cacheAdaptor = new ProductSearchCacheAdaptor(new ZarlockFrame());
      cacheAdaptor.setFilter("");
   }
}
