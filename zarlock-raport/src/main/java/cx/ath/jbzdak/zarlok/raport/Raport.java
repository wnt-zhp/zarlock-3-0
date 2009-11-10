package cx.ath.jbzdak.zarlok.raport;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.config.PreferencesConfig;
import cx.ath.jbzdak.zarlok.config.PreferencesKeys;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.Desktop.Action;
import java.io.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Apr 27, 2009
 */
public abstract class Raport {

   private static final String IO_EXCEPTION_MESSAGE
           = "Błąd powiązany z zapisem dokumentu do pliku, zmień nazwę, może się mu polepszy";

   private static final String DOC_EXCEPTION_MESSAGE
           = "Błąd powiązany z tworzeniem dokumentu, prawdopodobnie wynika on z błędu programisty. " +
           "Możesz spróbować jeszcze raz, ale raczej nie pomoże. . .";

   private static final Logger LOGGER = Utils.makeLogger();

   protected final Font defaultHeaderFont;
   {
      try {
         defaultHeaderFont = new Font(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, true), 17, Font.BOLD);
      } catch (Exception e){
         LOGGER.warn("Exception occoured", e);
         throw new RuntimeException(e);
      }
   }

   protected final Font defaultParagraphFont;
   {
      try {
         defaultParagraphFont = new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, true), 12, Font.NORMAL);
      } catch (Exception e){
         LOGGER.warn("Exception occoured", e);
         throw new RuntimeException(e);
      }
   }

    protected final Font boldParagraphFont;
   {
      boldParagraphFont = new Font(defaultParagraphFont);
      boldParagraphFont.setStyle(Font.BOLD);
   }

   private OutputStream resultStream;

   protected Document document;

   protected DocWriter writer;

   private TargetType type = TargetType.PDF;

   protected Object[] data;

   protected abstract OutputStream createOstraeam();

   protected abstract void makeReport() throws DocumentException;

   protected void addHeader() throws DocumentException{
      document.add(new Paragraph(new Chunk(PreferencesConfig.getConfigurationSource().getConfiguration().get(PreferencesKeys.NAZWA_OBOZU_1).getSingleValue().toString(), defaultParagraphFont)));
      document.add(new Paragraph(new Chunk(PreferencesConfig.getConfigurationSource().getConfiguration().get(PreferencesKeys.NAZWA_OBOZU_2).getSingleValue().toString(), defaultParagraphFont)));
      document.add(new Paragraph(new Chunk(PreferencesConfig.getConfigurationSource().getConfiguration().get(PreferencesKeys.NAZWA_OBOZU_3).getSingleValue().toString(), defaultParagraphFont)));
   }

   public void print() throws ReportException {
      if((!Desktop.isDesktopSupported()) || (! Desktop.getDesktop().isSupported(Action.PRINT) && ! Desktop.getDesktop().isSupported(
              Action.OPEN)) ){
         throw new ReportExceptionForUser("Nie wspieramy drukowania na tym systemie, ciagle " +
                 "możesz zapisać dokumenty jako pliki pdf i wydrukowac je ręcznie. Jeśli korzystasz z linuksa" +
                 "być może brakuje bibliotek GTK, równie dobrze drukowanie może w ogóle nie być wspierane... " +
                 " Najprościej - zapisz raport do pliku i wydrukuj z poziomu czytnika .pdf.");

      }
      File resultFile = Utils.createTmpFile(type.getExtension());
      try {
         setResultFile(resultFile);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e); //Nie zdarzy się raczej
      }
      try {
         makeReport();
         if(Desktop.getDesktop().isSupported(Action.PRINT)){
         Desktop.getDesktop().print(resultFile);
         }else{
            Desktop.getDesktop().open(resultFile);
         }
      } catch (DocumentException e) {
         throw new ReportException(DOC_EXCEPTION_MESSAGE,e);
      } catch (IOException e) {
         throw new ReportExceptionForUser(IO_EXCEPTION_MESSAGE, e);
      }
   }

    protected PdfPCell getDefaultCell(String phrase){
      Chunk txt = new Chunk(phrase);
      txt.setFont(defaultParagraphFont);
      return getDefaultCell(new Phrase(txt));
   }

   protected PdfPCell getDefaultCell(Phrase phrase){
      PdfPCell result = new PdfPCell(phrase);
      result.setBorder(Cell.LEFT | Cell.RIGHT | Cell.TOP | Cell.BOTTOM);
      result.setBorderWidth(1f);
      result.setHorizontalAlignment(Cell.ALIGN_CENTER);
      return result;
   }

    protected PdfPCell getHeaderCell(String headerText){
      Chunk txt = new Chunk(headerText, boldParagraphFont);
      PdfPCell result = getDefaultCell(new Phrase(txt));
      result.setBackgroundColor(Color.LIGHT_GRAY);
      result.setHorizontalAlignment(Cell.ALIGN_CENTER);
      result.setBorder(Cell.LEFT | Cell.RIGHT | Cell.TOP | Cell.BOTTOM);
      result.setBorderWidth(2f);
      return result;
   }

   public void save(File file) throws ReportException {
      try {
         file.createNewFile();
         setResultFile(file);
         makeReport();
      } catch (IOException e) {
         throw new ReportExceptionForUser(IO_EXCEPTION_MESSAGE, e);
      } catch (DocumentException e) {
         throw  new ReportException(DOC_EXCEPTION_MESSAGE, e);
      }
   }

   protected void init() throws DocumentException {
      document = new Document(PageSize.A4, 80, 36, 36, 36);
      writer = type.createWriter(document, getResultStream());
   }

   protected OutputStream getResultStream() {
      if (resultStream == null) {
        resultStream = createOstraeam();
      }
      return resultStream;
   }

   public void setResultStream(OutputStream resultStream) {
      if(this.resultStream != null){
         throw new IllegalStateException();
      }
      this.resultStream = resultStream;
   }

   public void setResultFile(File result) throws FileNotFoundException {
      if(resultStream != null){
         throw new IllegalStateException();
      }
      resultStream = new FileOutputStream(result);
   }

   public void setBackedByArray(){
      if(resultStream != null){
         throw new IllegalStateException();
      }
      resultStream = new ByteArrayOutputStream();
   }

   public Object[] getData() {
      return data;
   }

   public void setData(Object... data) {
     this.data = data;
   }

   public TargetType getType() {
      return type;
   }

   public void setType(TargetType type) {
      this.type = type;
   }
}
