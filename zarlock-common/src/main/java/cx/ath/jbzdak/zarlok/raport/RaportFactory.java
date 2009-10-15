package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.zarlok.entities.Dzien;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-14
 */
public interface RaportFactory {
   void printStanMagazynu(Dzien d)throws RaportException;

   void saveStanMagazynu(Dzien d)throws RaportException;

   void printZZ(Dzien d) throws RaportException;

   void saveZZ(Dzien d) throws RaportException;

   void saveKartoteki() throws RaportException;

   void saveDokumentacja() throws RaportException;
}
