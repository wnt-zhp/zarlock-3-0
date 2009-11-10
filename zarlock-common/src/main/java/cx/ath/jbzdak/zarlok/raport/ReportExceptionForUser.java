package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.jpaGui.ExceptionForUser;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-05
 */
public class ReportExceptionForUser extends ReportException implements ExceptionForUser{
   public ReportExceptionForUser(String message) {
      super(message);
   }

   public ReportExceptionForUser(String message, Throwable cause) {
      super(message, cause);
   }

   public ReportExceptionForUser(Throwable cause) {
      super(cause);
   }
}
