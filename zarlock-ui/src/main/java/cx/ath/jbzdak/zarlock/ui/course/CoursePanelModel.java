package cx.ath.jbzdak.zarlock.ui.course;

import org.jdesktop.observablecollections.ObservableList;
import org.apache.commons.math.util.MathUtils;

import java.util.List;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import cx.ath.jbzdak.zarlok.entities.Expenditure;
import cx.ath.jbzdak.zarlok.entities.Course;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.common.annotation.property.Bound;
import cx.ath.jbzdak.common.ActionSupport;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 30, 2010
 */
public class CoursePanelModel {

   public static final String EXPENDITURES_CHANGED = "EXPENDITURES_CHANGED";
   public static final int EXPENDITURES_CHANGED_ID = 1;



   final ActionSupport actionSupport = new ActionSupport(this);

   @Bound
   ObservableList<Expenditure> expenditures;

   //Course course;

   final DAO<Course> courseDAO;

   final DAO<Expenditure> expenditureDAO;

   public CoursePanelModel(CoursePanel coursePanel) {
      DBManager<?> dbHolder = ZarlockUtils.getZarlockModel(coursePanel).getDBManager();
      courseDAO = dbHolder.getDao(Course.class);
      expenditureDAO = dbHolder.getDao(Expenditure.class);
   }

   public void addExpenditure(Expenditure expenditure){
      expenditure.setCourse(courseDAO.getBean());
      expenditureDAO.setBean(expenditure);
      expenditureDAO.persist();
      courseDAO.find(courseDAO.getBean().getId());
      actionSupport.fireActionEvent(EXPENDITURES_CHANGED, EXPENDITURES_CHANGED_ID);
   }
   
   /**
    *
    * @param index index w getCourse.getExpenditures 
    */
   public void removeExpenditure(int index){
      removeExpenditure(getCourse().getExpenditures().get(index));
   }


   public BigDecimal getMaxQuantity(Batch batch){
      return Utils.round(batch.getStartQty().subtract(batch.getCurrentQty()), 2);
   }

   

   public void removeExpenditure(Expenditure expenditure){
      expenditureDAO.setBean(expenditure);
      expenditureDAO.remove();
      courseDAO.find(courseDAO.getBean().getId());
      actionSupport.fireActionEvent(EXPENDITURES_CHANGED, EXPENDITURES_CHANGED_ID);
   }

   @Bound
   public Course getCourse() {
      return courseDAO.getBean();
   }

   public void setCourse(Course bean) {
      courseDAO.setBean(bean);
   }

   // ...oo000ooo... Stupid accessors

   public void addActionListener(ActionListener actionListener) {
      actionSupport.addActionListener(actionListener);
   }

   public List<ActionListener> getActionListeners() {
      return actionSupport.getActionListeners();
   }

   public boolean hasActionListeners() {
      return actionSupport.hasActionListeners();
   }

   public boolean removeActionListener(Object o) {
      return actionSupport.removeActionListener(o);
   }
}
