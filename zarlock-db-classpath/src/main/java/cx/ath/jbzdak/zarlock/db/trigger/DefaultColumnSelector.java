package cx.ath.jbzdak.zarlock.db.trigger;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
@ThreadSafe
public class DefaultColumnSelector implements ColumnSelector{

   final Map<String, Integer> columnMapper;

   public DefaultColumnSelector(Map<String, Integer> columnMapper) {
      this.columnMapper = columnMapper;
   }


   @Override
   public int getOrdinalForName(String name) {
      return columnMapper.get(name);
   }
}
