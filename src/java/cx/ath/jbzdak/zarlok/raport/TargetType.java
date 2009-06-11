package cx.ath.jbzdak.zarlok.raport;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

import java.io.OutputStream;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Apr 27, 2009
 */
public enum TargetType {
   PDF(".pdf"){
      @Override
      public DocWriter createWriter(Document doc, OutputStream os) throws DocumentException {
         return PdfWriter.getInstance(doc, os);
      }
   }, RTF(".rtf"){
      @Override
      public DocWriter createWriter(Document doc, OutputStream os) throws DocumentException {
         return RtfWriter2.getInstance(doc, os);
      }};

   private final String extension;

   TargetType(String extension) {
      this.extension = extension;
   }

   public String getExtension() {
      return extension;
   }

   public abstract DocWriter createWriter(Document doc, OutputStream os) throws DocumentException;
}
