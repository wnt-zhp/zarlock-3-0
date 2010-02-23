package cx.ath.jbzdak.zarlok.entities.xml;

import cx.ath.jbzdak.zarlok.entities.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-26
 */
@XmlRootElement(namespace = "http://skimbleshanks.ath.cx/zarlock/schema/3.1", name="zarlock-store")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XMLStore {

   @XmlAttribute
   Long storeVersion;

   private List<ConfigEntry> configuration;

   private List<Product> products;

   private List<Batch> batches;

   private List<Expenditure> expenditures;

   private  List<Day> days;

   private List<Meal> meals;

   private List<Course> courses;

   @XmlElementWrapper(name = "products")
   @XmlElement(name = "product")
   public List<Product> getProducts() {
      return products;
   }

   @XmlElementWrapper(name = "batches")
   @XmlElement(name = "batch")
   public List<Batch> getBatches() {
      return batches;
   }

   @XmlElementWrapper(name = "configuration")
   @XmlElement(name = "configEntry")
   public List<ConfigEntry> getConfiguration() {
      return configuration;
   }

    @XmlElementWrapper(name = "courses")
   @XmlElement(name = "course")
   public List<Course> getCourses() {
      return courses;
   }

   @XmlElementWrapper(name = "days")
   @XmlElement(name = "day")
   public List<Day> getDays() {
      return days;
   }

   @XmlElementWrapper(name = "expenditures")
   @XmlElement(name = "expenditure")
   public List<Expenditure> getExpenditures() {
      return expenditures;
   }

   @XmlElementWrapper(name = "meals")
   @XmlElement(name = "meal")
   public List<Meal> getMeals() {
      return meals;
   }

   public void setBatches(List<Batch> batches) {
      this.batches = batches;
   }

   public void setProducts(List<Product> products) {
      this.products = products;
   }

   public void setConfiguration(List<ConfigEntry> configuration) {
      this.configuration = configuration;
   }

   public void setCourses(List<Course> courses) {
      this.courses = courses;
   }

   public void setDays(List<Day> days) {
      this.days = days;
   }

   public void setExpenditures(List<Expenditure> expenditures) {
      this.expenditures = expenditures;
   }

   public void setMeals(List<Meal> meals) {
      this.meals = meals;
   }
}
