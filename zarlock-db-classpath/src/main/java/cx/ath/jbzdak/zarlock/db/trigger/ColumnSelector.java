package cx.ath.jbzdak.zarlock.db.trigger;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
public interface ColumnSelector {

   int getOrdinalForName(String name);

   
}
