package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.zarlok.entities.Dzien;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-14
 */
public interface ReportFactory {
   void printStanMagazynu(Dzien d)throws ReportException;

   void saveStanMagazynu(Dzien d)throws ReportException;

   void printZZ(Dzien d) throws ReportException;

   void saveZZ(Dzien d) throws ReportException;

   void saveKartoteki() throws ReportException;

   void saveDokumentacja() throws ReportException;
}
