package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.jpaGui.ExceptionForUser;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-05
 */
public class RaportExceptionForUser extends RaportException implements ExceptionForUser{
   public RaportExceptionForUser(String message) {
      super(message);
   }

   public RaportExceptionForUser(String message, Throwable cause) {
      super(message, cause);
   }

   public RaportExceptionForUser(Throwable cause) {
      super(cause);
   }
}
