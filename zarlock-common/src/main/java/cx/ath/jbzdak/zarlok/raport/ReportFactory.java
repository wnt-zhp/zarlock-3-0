package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.zarlok.entities.Day;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-14
 */
public interface ReportFactory {
   void printStanMagazynu(Day d)throws ReportException;

   void saveStanMagazynu(Day d)throws ReportException;

   void printZZ(Day d) throws ReportException;

   void saveZZ(Day d) throws ReportException;

   void saveKartoteki() throws ReportException;

   void saveDokumentacja() throws ReportException;
}
