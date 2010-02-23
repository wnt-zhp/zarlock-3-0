package cx.ath.jbzdak.zarlok.entities.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-27
 */
public class IdAdapter extends XmlAdapter<String, Long> {

   private final String prefix;

   public IdAdapter(String prefix) {
      this.prefix = prefix;
   }

   @Override
   public String marshal(Long v) throws Exception {
      return prefix + v;
   }

   @Override
   public Long unmarshal(String v) throws Exception {
      if(!v.startsWith(prefix)){
         throw new IllegalArgumentException();
      }
      return Long.valueOf(v.substring(prefix.length()));
   }
}
