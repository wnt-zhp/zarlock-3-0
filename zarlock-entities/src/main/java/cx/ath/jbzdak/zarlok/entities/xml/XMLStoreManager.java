package cx.ath.jbzdak.zarlok.entities.xml;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.*;


import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-27
 */
public class XMLStoreManager {

   XMLStore xmlStore;

   EntityManager entityManager;


   public static XMLStore loadStore(File file) throws JAXBException, FileNotFoundException {
      return loadStore(new FileInputStream(file));
   }

   public static XMLStore loadStore(InputStream istr) throws JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(XMLStore.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      XMLStore xmlStore = (XMLStore) unmarshaller.unmarshal(istr);
      return xmlStore;
   }

   public static void save(File target, XMLStore store) throws JAXBException, IOException {
      save(new FileOutputStream(target), store);
   }


   public static void save(OutputStream target, XMLStore store) throws JAXBException, IOException {
      JAXBContext jaxbContext = JAXBContext.newInstance(XMLStore.class);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(store, target);
      target.flush();
   }

   public static void load(File f, DBManager dbManager) throws JAXBException, FileNotFoundException {
      load(new FileInputStream(f), dbManager);
   }

   public static void load(InputStream istr, DBManager dbManager) throws JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(XMLStore.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      XMLStore xmlStore = (XMLStore) unmarshaller.unmarshal(istr);
      load(xmlStore, dbManager);
   }

   public static void load(XMLStore store, DBManager<EntityManager> dbManager){
      EntityManager entityManager = dbManager.createProvider();
      try{
         load(store, entityManager);
      }finally {
         entityManager.close();
      }
   }

   public static void load(XMLStore store, EntityManager entityManager){
      persistCollection(store.getConfiguration(), entityManager);
      persistCollection(store.getProducts(), entityManager);
      persistCollection(store.getBatches(), entityManager);
      persistCollection(store.getDays(), entityManager);
      persistCollection(store.getMeals(), entityManager);
      persistCollection(store.getCourses(), entityManager);
      persistCollection(store.getExpenditures(), entityManager);
   }

   private static void persistCollection(Collection<?> coll, EntityManager entityManager){
      entityManager.getTransaction().begin();
      try{
         for(Object o : coll){
            entityManager.merge(o);
         }
         entityManager.getTransaction().commit();
      }catch (RuntimeException e){
         entityManager.getTransaction().rollback();
      }
   }

   public XMLStoreManager(DBManager<EntityManager> dbManager){
      entityManager = dbManager.createProvider();
      xmlStore = new XMLStore();
//      xmlStore.setProducts(entityManager.createQuery("SELECT p FROM Produkt p").getResultList());
//      xmlStore.setBatches(entityManager.createQuery("SELECT p FROM Partia p").getResultList());
//      xmlStore.setConfiguration(entityManager.createQuery("SELECT ce FROM ConfigEntry ce").getResultList());
//      xmlStore.setExpenditures(loadSafe(Wyprowadzenie.class, entityManager));
//      xmlStore.setDays(entityManager.createQuery("SELECT d FROM Dzien d").getResultList());
//      xmlStore.setCourses(loadSafe(Danie.class, entityManager));
//      xmlStore.setMeals(entityManager.createQuery("SELECT d FROM Posilek d").getResultList());
   }

   public <T> List<T> loadSafe(Class<T> clazz, EntityManager entityManager){
      List<Long> ids = entityManager.createQuery("SELECT o.id FROM " + clazz.getCanonicalName() + " o").getResultList();
      List<T> result = new ArrayList<T>(ids.size());
      for(Long id : ids){
         try{
            T t = (T)entityManager.createQuery("SELECT o FROM " + clazz.getCanonicalName() + " o WHERE o.id = :id").setParameter("id", id).getSingleResult();
            for(Method m : clazz.getDeclaredMethods()){
               if(m.getName().startsWith("get") && m.getParameterTypes().length==0){
                  m.invoke(t);
               }
            }
            result.add(t);
         }catch (Exception e){
            e.printStackTrace();
         }
      }
      return result;
   }

   public XMLStore getXmlStore() {
      return xmlStore;
   }

   public void close(){
      entityManager.close();
   }
}
