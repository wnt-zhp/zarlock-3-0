package cx.ath.jbzdak.zarlok.entities.xml;

import cx.ath.jbzdak.jpaGui.utils.DBUtils;
import cx.ath.jbzdak.zarlok.entities.*;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-27
 */
public class XMLLoader {

   final Map<Long, Long> daysMapper = new HashMap<Long, Long>();

   final Map<Long, Long> productIdMapper = new HashMap<Long, Long>();

   final Map<Long, Long> batchIdMapper = new HashMap<Long, Long>();

   final Map<Long, Long> mealMapper = new HashMap<Long, Long>();

   final Map<Long, Long> courseMapper = new HashMap<Long, Long>();


   final EntityManager entityManager;


   final XMLStore xmlStore;

   public XMLLoader(EntityManager entityManager, XMLStore xmlStore) {
      this.entityManager = entityManager;
      this.xmlStore = xmlStore;
   }

   public void load(){
      loadConfig();
      loadProducts();
      loadBatches();
      loadDays();
      loadMeals();
      loadCourses();
      loadExpenditures();
   }

   /**
    * Magia jest taka, a to jest wysoka magia. . .
    *
    * Przed persysowaniem każgego jednego obiektu ustawiamy jego Id na null,
    * w ten sposób Id samo się wygeneruje (nie wspieramy nie autogenerowalnych id).
    * Metoda persist ustawi ida ponieważ JAXB zasadniczo buduje całe drzewo obiektów Javy,
    *  to owo ID będzie ustawione na dobre id we wszystkich obiektach potomnych. Magia!
    *
    * Teraz tylko trzeba pilnować żeby w dobrej kolejności persystować obiekty, tj:
    * jeśli Batch zawiera odniesienie do Productu to product musi byś spersystowany pierwsz
    * quick and dirty!.
    * @param objects
    */
   public void loadCollection(Collection<?> objects){
      for(Object o : objects){
         DBUtils.setId(o, null, Long.class);
         entityManager.persist(o);
      }
   }

   public void loadMeals(){
      loadCollection(xmlStore.getMeals());
   }

   public void loadCourses(){
      loadCollection(xmlStore.getCourses());
   }

   public void loadExpenditures(){
     loadCollection(xmlStore.getExpenditures());
   }

   public void loadDays(){
    loadCollection(xmlStore.getDays());
   }

   public void loadBatches(){
      loadCollection(xmlStore.getBatches());
   }

   public void loadProducts(){
   loadCollection(xmlStore.getProducts());

   }

   public void loadConfig(){
   loadCollection(xmlStore.getConfiguration());
   }

   private static interface TransactionExec{
      void doTransaction(EntityManager entityManager);
   }
}
