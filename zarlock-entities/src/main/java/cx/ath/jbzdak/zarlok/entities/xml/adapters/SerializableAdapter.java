package cx.ath.jbzdak.zarlok.entities.xml.adapters;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-27
 */
public class SerializableAdapter extends XmlAdapter<String, Object> {
   @Override
   public String marshal(Object v) throws Exception {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
      objectOutputStream.writeObject(v);
      objectOutputStream.flush();
      return DatatypeConverter.printBase64Binary(stream.toByteArray());
   }

   @Override
   public Object unmarshal(String v) throws Exception {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(v));
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      return objectInputStream.readObject();
   }
}
