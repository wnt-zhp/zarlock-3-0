package cx.ath.jbzdak.zarlok.db;

import cx.ath.jbzdak.jpaGui.Utils;
import org.slf4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-21
 */
public class DBBackup {

   private static final Pattern BACKUP_FILE_PATTERN
           = Pattern.compile("\\d{2}-\\d{2}-\\d{4}(?:_(\\d+))?\\.zip");

   private final DateFormat backupFileFormat
           = new SimpleDateFormat("dd-MM-yyyy");

   private static final Logger LOGGER = Utils.makeLogger();

//   private final DBSetup setup;
//
//   private final DBClose close;

   @SuppressWarnings({"WeakerAccess"})
   ZarlockDBManager manager;


   public DBBackup() {
//      this.setup = setup;
//      this.close = close;
   }

   @SuppressWarnings({"WeakerAccess"})
   ZipOutputStream outputStream;

   private File openZipFile(){
      File backup = new File(manager.getDatabaseBackupFolder());
      final Pattern todayPattern =
              Pattern.compile(backupFileFormat.format(new Date()) + "(?:_(\\d+))?\\.zip");
      String[] names = backup.list(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            return todayPattern.matcher(name).matches();
         }
      });
      int maxId = -1;
      for (String name : names) {
         Matcher m = BACKUP_FILE_PATTERN.matcher(name);
         if(m.matches()){
            String idString = m.group(1);
            int id;
            try {
               id = Integer.valueOf(idString);
            } catch (NumberFormatException e) {
               LOGGER.warn("",e);
               continue;
            }
            if(maxId<id){
               maxId = id;
            }
         }
      }
      File zipFile =new File(backup, backupFileFormat.format(new Date()) + "_" + ++maxId + ".zip");
      try {
         zipFile.createNewFile();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      return zipFile;
   }

   private void appendFile(File file) throws IOException {
      if(!file.exists()){
         return; //OK może się zdarzyć
      }
      outputStream.putNextEntry(new ZipEntry(file.getName()));
      BufferedInputStream istr = null;
      try {
         istr = new BufferedInputStream(new FileInputStream(file));

         byte[] buffer = new byte[1024];
         int read;
         while((read = istr.read(buffer))!=-1){
            outputStream.write(buffer,0,read);
         }
         outputStream.flush();
      } finally {
         if(istr!=null){
            istr.close();
         }
      }
   }

   public void doBackup(ZarlockDBManager manager){
      this.manager = manager;
      doBackup(manager, openZipFile());
   }

    public void doBackup(ZarlockDBManager manager, File target){
      this.manager = manager;
      try {
         manager.checkpoint();
      } catch (SQLException e) {
         LOGGER.error("While doing checkpoint", e);
      }
      makeBackupCopy(manager, target);
   }

   void makeBackupCopy(ZarlockDBManager manager){
      this.manager = manager;
      makeBackupCopy(manager, openZipFile());
   }

   void makeBackupCopy(ZarlockDBManager manager, File target){
      this.manager = manager;
      try {
         outputStream = new ZipOutputStream(new FileOutputStream(target));
         for(File f: manager.getDatabaseFiles()){
            appendFile(f);
         }
         outputStream.closeEntry();
         outputStream.close();
      } catch (IOException e) {
         LOGGER.error("", e);
         throw new RuntimeException(e);
      }
   }

   @edu.umd.cs.findbugs.annotations.SuppressWarnings({"RV_RETURN_VALUE_IGNORED_BAD_PRACTICE "})
   public void readFromBackup(File zipFile, final ZarlockDBManager manager) throws IOException {
      if(manager.isDatabaseOpened()){
         throw new IllegalStateException("Najpierw zamknij bazę danych");
      }
      makeBackupCopy(manager, openZipFile());
      File dbFolder = new File(manager.getDatabaseFolder());
      File[] toDelete =  dbFolder.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            return name.startsWith(manager.getDatabasePrefix());
         }
      });
      for(File f : toDelete){
         if(!f.delete()){
            LOGGER.warn("Coulndt delete file {} while reading backup", f.getName());
         }
      }
      ZipFile zip = new ZipFile(zipFile);
      Enumeration<? extends ZipEntry> entries = zip.entries();

      byte buffer[] = new byte[1024];
      int read;
      while (entries.hasMoreElements()) {
         ZipEntry o =  entries.nextElement();
         File dbFile = new File(dbFolder, o.getName());
         if(dbFile.exists()){
            throw new IllegalStateException();
         }
         dbFile.createNewFile();
         InputStream istream = zip.getInputStream(o);
         BufferedOutputStream result = new BufferedOutputStream(new FileOutputStream(dbFile));
         while((read = istream.read(buffer)) != -1){
            result.write(buffer, 0, read);
         }
         result.close();
      }

   }


}
