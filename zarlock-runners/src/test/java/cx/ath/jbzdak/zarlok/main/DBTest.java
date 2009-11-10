package cx.ath.jbzdak.zarlok.main;

import org.junit.Test;
import org.junit.BeforeClass;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.ConfigEntry;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import junit.framework.Assert;

import javax.persistence.PersistenceException;
import java.rmi.server.UID;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-20
 */
public class DBTest {

   @BeforeClass
   public static void beforeClass(){
      new TstAPPLauncher().start();
   }

   @Test
   public void testConfigEntry(){
      DAO<ConfigEntry> dao = DBHolder.getDbManager().getDao(ConfigEntry.class);
      dao.setAutoCreateEntity(true);
      String name = new UID().toString();
      dao.getBean().setName(name);
      dao.getBean().setEditable(false);
      dao.persist();
      Long id = dao.getBean().getId();
      DAO<ConfigEntry> dao2 =  DBHolder.getDbManager().getDao(ConfigEntry.class);
      dao2.find(id);
      Assert.assertEquals(dao2.getBean().getName(), name);
   }

   @Test(expected = PersistenceException.class)
   public void testConfigEntry2(){
      DAO<ConfigEntry> dao = DBHolder.getDbManager().getDao(ConfigEntry.class);
      dao.setAutoCreateEntity(true);
      dao.getBean().setName(new UID().toString());
      dao.getBean().setEditable(false);
      dao.persist();
      dao.getBean().setDesctiption("dasdas");
      dao.update();
   }


}
