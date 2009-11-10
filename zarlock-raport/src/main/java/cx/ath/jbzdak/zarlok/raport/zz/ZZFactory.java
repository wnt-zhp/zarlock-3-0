package cx.ath.jbzdak.zarlok.raport.zz;

import cx.ath.jbzdak.zarlok.config.Preferences;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.raport.ReportException;
import cx.ath.jbzdak.zarlok.raport.ReportExceptionForUser;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-05
 */
public class ZZFactory {

   public void printZZ(Dzien d) throws ReportException {
      ZZRaport raport = new ZZRaport();
     ZZRaportBean zzRaportBean = new ZZRaportBean(d);

     raport.setData(zzRaportBean);
     raport.print();
   }

   public void saveZZ(Dzien d) throws ReportException {
     ZZRaport raport = new ZZRaport();
      ZZRaportBean zzRaportBean = new ZZRaportBean(d);
      File file = new File(Preferences.getZapotrzebowaniaFolder(), "ZZ" + new SimpleDateFormat("yyyy-MM-dd").format(d.getData()) +".pdf");
      raport.setData(zzRaportBean);
      raport.save(file);
   }

     public void cleanZZFolder() throws ReportExceptionForUser {
      for(String filename : Preferences.getZapotrzebowaniaFolder().list()){
         if(!new File(Preferences.getZapotrzebowaniaFolder(), filename).delete()){
            throw new ReportExceptionForUser("Nie udało się usunąć starego pliku z zapotrzebowaniem, " +
                    "możliwe że jest on otwarty w innym programie. Zamknij go i spróbuj ponownie.\nNazwa pliku to :'"
            +filename + "'");
         }
      }
   }

}
