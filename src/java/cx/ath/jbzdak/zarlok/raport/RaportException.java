package cx.ath.jbzdak.zarlok.raport;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-28
 */
@SuppressWarnings({"WeakerAccess"})
public class RaportException extends Exception{
   public RaportException(String message) {
      super(message);
   }

   public RaportException(String message, Throwable cause) {
      super(message, cause);
   }

   public RaportException(Throwable cause) {
      super(cause);
   }
}
