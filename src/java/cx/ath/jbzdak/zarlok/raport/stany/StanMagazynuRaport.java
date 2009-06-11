package cx.ath.jbzdak.zarlok.raport.stany;

import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter.formatMessage;
import cx.ath.jbzdak.zarlok.raport.Raport;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-04
 */
class StanMagazynuRaport extends Raport{

   private StanMagazynuRaportBean stanMagazynuRaportBean;
  @Override
   protected OutputStream createOstraeam() {
      File tmpFile = Utils.createTmpFile(".pdf");
      try {
         return new FileOutputStream(tmpFile);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
         //Nie zdarzy się raczej
      }
   }



   @Override
   protected void makeReport() throws DocumentException {
      stanMagazynuRaportBean = (StanMagazynuRaportBean) data[0];
      init();
      document.addTitle(formatMessage("Stan magazynu na dzień " +
              "na dzień {dzien.data}", data));
      document.addCreationDate();
      document.addCreator("Żarłok - zarządzanie magazynem żywnościowym - wersja 2.0, za pomocą biblioteki iText");
      document.addKeywords("zhp, harcerstwo, zz, zapotrzebowanie żywnościowe");
      document.open();
      addHeader();
      Paragraph stanHeader = new Paragraph(formatMessage("Stan magazynu na dzień {dzien.data}", data), defaultHeaderFont);
      stanHeader.setAlignment(Paragraph.ALIGN_CENTER);
      document.add(stanHeader);
      document.add(new Paragraph(new Chunk("\n")));
      addTable();
      document.close();
   }

   private void addTable() throws DocumentException {
      PdfPTable table = new PdfPTable(new float[]{6,2,2} );
      table.addCell(getHeaderCell("Produkt"));
      table.addCell(getHeaderCell("Jednostka"));
      table.addCell(getHeaderCell("Ilość"));
      table.setHeaderRows(1);
      for(StanMagazynuEntryBean bean : this.stanMagazynuRaportBean.getPartie()){
         table.addCell(getDefaultCell(formatMessage("{produkt.nazwa} {specyfikator}", bean)));
         table.addCell(getDefaultCell(bean.getJednostka()));
         table.addCell(String.valueOf(Utils.round(bean.getIloscJednostek(), 2)));
      }
      document.add(table);
   }
}
