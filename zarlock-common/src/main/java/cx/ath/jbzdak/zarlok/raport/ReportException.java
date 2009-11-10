package cx.ath.jbzdak.zarlok.raport;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-28
 */
@SuppressWarnings({"WeakerAccess"})
public class ReportException extends Exception{
   public ReportException(String message) {
      super(message);
   }

   public ReportException(String message, Throwable cause) {
      super(message, cause);
   }

   public ReportException(Throwable cause) {
      super(cause);
   }
}
