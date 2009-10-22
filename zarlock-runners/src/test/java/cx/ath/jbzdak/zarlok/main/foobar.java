package cx.ath.jbzdak.zarlok.main;

import java.net.URL;
import java.util.Enumeration;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-22
 */
public class foobar {

   public static void main(String[] strings) throws Exception{
      Enumeration<URL> url = foobar.class.getClassLoader().getResources("/updateSchemas");
      System.out.println(foobar.class.getClassLoader());
      while(url.hasMoreElements()){
         System.out.println(url.nextElement());
      }
   }
}
