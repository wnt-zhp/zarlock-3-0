package cx.ath.jbzdak.zarlok.raport.zz;

import cx.ath.jbzdak.jpaGui.app.ConfigEntry;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlok.config.PreferencesConfig;
import cx.ath.jbzdak.zarlok.config.PreferencesKeys;
import cx.ath.jbzdak.zarlok.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Apr 27, 2009
 */
public class ZZRaportBean {

   private final Dzien dzien;

   private final Map<String, ? extends ConfigEntry> dictionary;

   private int rowIdx = 0;

   private int maxRowIdx = 0;

   private final List<List<Wyprowadzenie>> posilki;

   private final List<List<Wyprowadzenie>> posilkiDodatkowe;

   private final List<String> posilkiNames;
   private final List<String> posilkiDodatkoweNames;


   private final PatternBeanFormatter wyprowadzeniaFormatter
           = new PatternBeanFormatter("{partia.nazwaProduktu} {partia.specyfikator} {iloscJednostek} {partia.jednostka}");

   public ZZRaportBean(Dzien dzien) {
      this.dzien = dzien;
      this.dictionary = PreferencesConfig.getConfigurationSource().getConfiguration();
      List<Posilek> posilki = new ArrayList<Posilek>(), posilkiDodatkowe = new ArrayList<Posilek>();

      for(Posilek p : dzien.getPosilki()){
         if(p.getDodatkowy()){
            posilkiDodatkowe.add(p);
         }else{
            posilki.add(p);
         }
      }
      this.posilkiDodatkowe = new ArrayList<List<Wyprowadzenie>>(posilkiDodatkowe.size());
      this.posilki = new ArrayList<List<Wyprowadzenie>>(posilki.size());
      this.posilkiNames = new ArrayList<String>(posilki.size());
      this.posilkiDodatkoweNames = new ArrayList<String>(posilkiDodatkowe.size());
      for (int ii = 0; ii < posilki.size(); ii++) {
         Posilek posilek = posilki.get(ii);
         posilkiNames.add(posilek.getNazwa());
         List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();
         this.posilki.add(wyprowadzenia);
         for (Danie danie : posilek.getDania()) {
            for (Wyprowadzenie wyprowadzenie : danie.getWyprowadzenia()) {
               wyprowadzenia.add(wyprowadzenie);
            }
         }
      }
      for(Posilek p : posilkiDodatkowe){
         posilkiDodatkoweNames.add(p.getNazwa());
         List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();
         for(Danie d : p.getDania()){
            wyprowadzenia.addAll(d.getWyprowadzenia());
         }
         this.posilkiDodatkowe.add(wyprowadzenia);
      }
      maxRowIdx = -1;
      for (List<Wyprowadzenie> list : this.posilki) {
         maxRowIdx = Math.max(list.size(), maxRowIdx);
      }
   }

   public Dzien getDzien() {
      return dzien;
   }

   public Map<String, ? extends  ConfigEntry> getDictionary() {
      return dictionary;
   }

   public String getStawkaZywieniowa(){
      return dictionary.get(PreferencesKeys.STAWKA_ŻYWIENIOWA).getSingleValue().toString();
   }

   public int getPosilkiNumber(){
      return posilki.size();
   }

   public String getPosilekName(int number){
      return posilkiNames.get(number);
   }

   public String getNextWyprowadzenieForPosilek(int number){
      List<Wyprowadzenie> wyp = posilki.get(number);
      if(wyp.size() <= rowIdx){
         return "";
      }
      return formatWyprowadzenie(wyp.get(rowIdx));
   }

   public boolean  incrementRow(){
      rowIdx++;
      return maxRowIdx > rowIdx;
   }

   public boolean isLastRow(){
      return maxRowIdx - 1 > rowIdx;
   }



   String formatWyprowadzenie(Wyprowadzenie wyprowadzenie){
      return wyprowadzeniaFormatter.format(wyprowadzenie);
   }

   public List<List<Wyprowadzenie>> getPosilkiDodatkowe() {
      return posilkiDodatkowe;
   }

   public String getNazwaPosilkuDoda(int ii){
      return posilkiDodatkoweNames.get(ii);
   }

   public String getImieKomendant(){
      return dictionary.get(PreferencesKeys.KOMENDANT).getSingleValue().toString();
   }

   public String getImieSprawzajacy(){
      return dictionary.get(PreferencesKeys.CZLONEK_RADY_OBOZU).getSingleValue().toString();
   }

   public String getImieKwatermistrza(){
      return dictionary.get(PreferencesKeys.KWATERMISTRZ).getSingleValue().toString();
   }

   public String get1Linia(){
      return dictionary.get(PreferencesKeys.NAZWA_OBOZU_1).getSingleValue().toString();
   }

   public String get2Linia(){
      return dictionary.get(PreferencesKeys.NAZWA_OBOZU_2).getSingleValue().toString();
   }

   public String get3Linia(){
      return dictionary.get(PreferencesKeys.NAZWA_OBOZU_3).getSingleValue().toString();
   }

   public IloscOsob getIloscOsob() {return dzien.getIloscOsob();}

}

