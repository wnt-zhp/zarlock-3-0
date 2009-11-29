package cx.ath.jbzdak.zarlok.entities.xml;

import cx.ath.jbzdak.zarlok.entities.*;

import javax.persistence.EntityManager;
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

   
   public void loadMeals(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for (Meal m : xmlStore.getMeals()){
               Long id = m.getId();
               Long dayId = m.getDay().getId();
               m.setDay(entityManager.find(Day.class, daysMapper.get(dayId)));
               m.setId(null);
               entityManager.persist(m);
               mealMapper.put(id, m.getId());
            }
         }
      });
   }

   public void loadCourses(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for(Course course : xmlStore.getCourses()){
               Long id = course.getId();
               Long mealId = course.getMeal().getId();
               course.setMeal(entityManager.find(Meal.class, mealMapper.get(mealId)));
               course.setId(null);
               entityManager.persist(course);
               courseMapper.put(id, course.getId());
            }
         }
      });
   }

   public void loadExpenditures(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for(Expenditure e : xmlStore.getExpenditures()){
               e.setId(null);
               e.setBatch(entityManager.find(Batch.class, batchIdMapper.get(e.getBatch().getId())));
               if(e.getCourse()!=null){
                  e.setCourse(entityManager.find(Course.class, courseMapper.get(e.getCourse().getId())));
               }
               entityManager.persist(e);
            }
         }
      });
   }

   public void loadDays(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for(Day day : xmlStore.getDays()){
               Long id = day.getId();
               day.setId(null);
               entityManager.persist(day);
               daysMapper.put(id, day.getId());
            }
         }
      });
   }

   public void loadBatches(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for (Batch batch : xmlStore.getBatches()) {
               Long id = batch.getId();
               Long prodId = productIdMapper.get(batch.getProduct().getId());
               batch.setProduct(entityManager.find(Product.class, prodId));
               batch.setId(null);
               entityManager.persist(batch);
               batchIdMapper.put(id, batch.getId());
            }
         }
      });
   }

   public void loadProducts(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for(Product p : xmlStore.getProducts()){
               Long id = p.getId();
               p.setId(null);
               entityManager.persist(p);
               productIdMapper.put(id, p.getId());
            }
         }
      });

   }

   public void loadConfig(){
      executeTransaction(new TransactionExec() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            for(ConfigEntry configEntry : xmlStore.getConfiguration()){
               entityManager.persist(configEntry);
            }
         }
      });
   }

   private void executeTransaction(TransactionExec transaction){
//      entityManager.getTransaction().begin();
//      try{
         transaction.doTransaction(entityManager);
//         entityManager.getTransaction().commit();
//      }catch (RuntimeException e){
//         entityManager.getTransaction().rollback();
//         throw e;
//      }
   }

   private static interface TransactionExec{
      void doTransaction(EntityManager entityManager);
   }
}
