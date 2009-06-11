package cx.ath.jbzdak.zarlok.raport.kartoteki;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import static cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter.formatMessage;
import cx.ath.jbzdak.zarlok.raport.Raport;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: May 6, 2009
 */
class KartotekaRaport extends Raport{

   private KartotekaRaportBean kartotekaRaportBean;

   private final String kartotekaTitle =
          "Kartoteka magazynowa\nWartościowo ilościowa";

   private final String nazwaTowaruFormat =
           "Nazwa towaru: {nazwa} {specyfikator} cena: {cena}";

   private final PatternBeanFormatter nazwaProduktuFormatter
           = new PatternBeanFormatter(nazwaTowaruFormat);

   private final SimpleDateFormat format =
           new SimpleDateFormat("dd MM yyyy");

   @Override
   protected void makeReport() throws DocumentException {
      kartotekaRaportBean = (KartotekaRaportBean) data[0];
      init();
      document.addTitle(formatMessage(kartotekaTitle + "\n" + nazwaTowaruFormat , data));
      document.addCreationDate();
      document.addCreator("Żarłok - zarządzanie magazynem żywnościowym - wersja 2.0, za pomocą biblioteki iText");
      document.addKeywords("zhp, harcerstwo, zz, kartoteka magazynowa");
      document.open();
      addHeader();
      Paragraph header = new Paragraph(new Chunk(kartotekaTitle, defaultHeaderFont));
      header.setAlignment(Paragraph.ALIGN_CENTER);
      document.add(header);
      document.add(new Paragraph(new Chunk(nazwaProduktuFormatter.format(data),defaultParagraphFont)));
      document.add(new Paragraph(new Chunk("\n")));
      addTable();
      document.close();
   }

   private void addTable() throws DocumentException {
      PdfPTable table = new PdfPTable(9);
      createHeader(table);
      table.setHeaderRows(3);
      int lp = 0;
      for(KartotekaEntryBean row: kartotekaRaportBean.getZawartoscKartoteki()){
         table.addCell(getDefaultCell(String.valueOf(++lp)));
         table.addCell(getDefaultCell(format.format(row.getData()) + "r."));
         table.addCell(getDefaultCell(row.getNrDowodu()));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getPrzychodIlosc())));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getPrzychodWartosc())));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getRozchodIlosc())));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getRozchodWartosc())));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getStanIlosc())));
         table.addCell(getDefaultCell(formatBigDecimalColumn(row.getStanWartosc())));
      }
      document.add(table);
   }

   private String formatBigDecimalColumn(BigDecimal decimal){
      if(decimal!=null){
         return String.valueOf(Utils.round(decimal,2));
      }
      return "--------";
   }

   private void createHeader(PdfPTable table) {
      createFirstRow(table);
      createSecondRow(table);
      createThirdRow(table);
   }

   private void createFirstRow(PdfPTable table){
      PdfPCell lp = getDefaultCell("L.p.");
      lp.setBorder(Cell.TOP | Cell.LEFT | Cell.RIGHT);
      table.addCell(lp);
      PdfPCell data = getDefaultCell("data");
      data.setBorder(Cell.TOP | Cell.LEFT | Cell.RIGHT);
      table.addCell(data);
      PdfPCell nr = getDefaultCell("Symbol i nr. dowodu");
      nr.setBorder(Cell.TOP | Cell.LEFT | Cell.RIGHT);
      table.addCell(nr);
      PdfPCell przychod = getDefaultCell("Przychód");
      przychod.setColspan(2);
      table.addCell(przychod);
      PdfPCell rozchod = getDefaultCell("Rozchód");
      rozchod.setColspan(2);
      table.addCell(rozchod);
      PdfPCell stan = getDefaultCell("STAN");
      stan.setColspan(2);
      table.addCell(stan);
   }

   private void createSecondRow(PdfPTable table){
      PdfPCell cell = getDefaultCell("");
      cell.setBorder(Cell.LEFT | Cell.RIGHT | Cell.BOTTOM);
      table.addCell(new PdfPCell(cell));
      table.addCell(new PdfPCell(cell));
      table.addCell(new PdfPCell(cell));
      PdfPCell ilosc = getDefaultCell("Ilość");
      PdfPCell wartosc = getDefaultCell("Wartość");
      table.addCell(new PdfPCell(ilosc));
      table.addCell(new PdfPCell(wartosc));
      table.addCell(new PdfPCell(ilosc));
      table.addCell(new PdfPCell(wartosc));
      table.addCell(new PdfPCell(ilosc));
      table.addCell(new PdfPCell(wartosc));
   }

   private void createThirdRow(PdfPTable table){
      for(int ii=0; ii < 9; ii++){
         table.addCell(getDefaultCell(String.valueOf(ii)));
      }
   }

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
}
