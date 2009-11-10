package cx.ath.jbzdak.zarlok.raport.zz;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import static cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter.formatMessage;
import cx.ath.jbzdak.zarlok.entities.Expenditure;
import cx.ath.jbzdak.zarlok.raport.Raport;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Apr 27, 2009
 */
class ZZRaport extends Raport{

   private static final Logger LOGGER = Utils.makeLogger();

   private ZZRaportBean bean;

   private final PatternBeanFormatter kwatermistrz
           = new PatternBeanFormatter("Sporządził\n\n {imieKwatermistrza}\n\n Kwatermistrz lub zaopatrzeniowiec");


   private final PatternBeanFormatter sprawdzil
           = new PatternBeanFormatter("Sprawdzil\n\n {imieSprawzajacy}\n\n Czlonek rady Obozu");


   private final PatternBeanFormatter komendant
           = new PatternBeanFormatter("Zatwierdzil\n\n {imieKomendant}\n\n Komendant");

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
   public void makeReport() throws DocumentException {
      init();
      bean = (ZZRaportBean) data[0];
      document.addTitle(formatMessage("Zapotrzebowanie żywnościowe " +
              "na dzień {dzien.data}", data));
      document.addCreationDate();
      document.addCreator("Żarłok - zarządzanie magazynem żywnościowym - wersja 2.0, za pomocą biblioteki iText");
      document.addKeywords("zhp, harcerstwo, zz, zapotrzebowanie żywnościowe");
      document.open();
      addHeader();
      document.add(getHeader());
      document.add(getDate());
      document.add(getStawka());
      document.add(new Paragraph("\n"));
      addTable();
      addPosilkiDodatkowe();
      document.add(new Paragraph("Dane o stanie żywionych",boldParagraphFont));
      document.add(makeParagraph("1. Liczba uczestnikow obozu: {iloscOsob.iloscUczestnikow}\n" +
                   "2. Liczba kadry obozwej: {iloscOsob.iloscKadry}\n" +
                   "3. Inne osoby: {iloscOsob.iloscInnych}"));
      document.add(new Paragraph(formatMessage("Stan żywionych razem: {iloscOsob.suma}", data), boldParagraphFont));
      document.add(new Paragraph("\n"));
      document.add(makeParagraph("Przeciętny koszt wyżywienia jednej osoby {dzien.stawkaDzienna} zł."));
      addFooter();
      document.close();
   }

   private Chunk makeChunk(String message){
      return new Chunk(formatMessage(message, data), defaultParagraphFont);
   }

   private Paragraph makeParagraph(String message){
      return new Paragraph(makeChunk(message));
   }

   private Element getHeader(){
      Paragraph p = new Paragraph(new Chunk("Zapotrzebowanie żywnościowe",defaultHeaderFont));
      p.setAlignment(Element.ALIGN_CENTER);
      return p;
   }

   private Element getDate(){
      Paragraph p = new Paragraph(new Chunk(formatMessage("Data: {dzien.data}r.", data), defaultParagraphFont));
      return p;
   }

   private Element getStawka(){
      Font parFont = new Font(defaultParagraphFont);
      parFont.setStyle(Font.BOLD);
      Paragraph p = new Paragraph(new Chunk(formatMessage("Zatwierdzona w planie żywnościowym stawka " +
              "żywneniowa {stawkaZywieniowa} zł.", data),parFont));
      return p;
   }


   private void addTable() throws DocumentException {
      PdfPTable table = new PdfPTable(bean.getPosilkiNumber());
      for (int ii = 0; ii < bean.getPosilkiNumber(); ii++){
         table.addCell(getHeaderCell(bean.getPosilekName(ii)));
      }
      table.setHeaderRows(1);
      do{
         for (int ii = 0; ii < bean.getPosilkiNumber(); ii++){
         table.addCell(getDefaultCell(bean.getNextWyprowadzenieForPosilek(ii)));
      }
      }while (bean.incrementRow());
      document.add(table);
   }


   @Override
   protected PdfPCell getDefaultCell(Phrase phrase) {
      PdfPCell cell =  super.getDefaultCell(phrase);
      cell.setBorder(Cell.LEFT | Cell.RIGHT | (bean.isLastRow()?0:Cell.BOTTOM));
      return cell;
   }


//   private void addTable(Document document) throws DocumentException {
//      SimpleTable table = new SimpleTable();
//      SimpleCell cell = new SimpleCell(true);
//      PdfPTable t = new PdfPTable();
//
//      table.addElement(new );
//      table.setWidths(widths);
//      table.setBackgroundColor(Color.LIGHT_GRAY);
//
//      for(int ii =0; ii < bean.getPosilkiNumber(); ii++){
//         table.addCell(createHeaderCell(bean.getPosilekName(ii)));
//      }
//      table.endHeaders();
//      table.setBackgroundColor(Color.WHITE);
//      while(bean.incrementRow()){
//         for(int ii=0; ii < bean.getPosilkiNumber(); ii++){
//            table.addCell(createCell(bean.getNextWyprowadzenieForPosilek(ii)));
//         }
//         //table.flushContent();
//      }
//      //table.complete();
//      //table.flushContent();
//      document.add(table);
//   }

   private void addFooter() throws DocumentException {
      PdfPTable table = new PdfPTable(3);
      addFooterCell(table, kwatermistrz.format(bean));
      addFooterCell(table, sprawdzil.format(bean));
      addFooterCell(table, komendant.format(bean));
      document.add(table);
   }


   private void addPosilkiDodatkowe() throws DocumentException {
      if(bean.getPosilkiDodatkowe().size()==0) return;
      Font f = new Font(defaultParagraphFont);
      f.setStyle(f.getStyle() | Font.BOLD);
      if(bean.getPosilkiDodatkowe().size()!=0){
         document.add(new Paragraph( new Chunk("Posiłki dodatkowe: ", f)));
      }

      for(int ii =0; ii < bean.getPosilkiDodatkowe().size(); ii++){
         Paragraph par = new Paragraph();
         par.add(new Chunk(bean.getNazwaPosilkuDoda(ii) + ": ", f));
         for(Expenditure w : bean.getPosilkiDodatkowe().get(ii)){
           par.add(new Chunk(bean.formatWyprowadzenie(w) +",", defaultParagraphFont));
         }
         document.add(par);
      }
   }

   private void addFooterCell(PdfPTable t, String s){
      PdfPCell cell = getDefaultCell(s);
      cell.setBorder(0);
      t.addCell(cell);
   }
}
