package test.entities;

import cx.ath.jbzdak.zarlok.db.DBClose;
import cx.ath.jbzdak.zarlok.db.DBSetup;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import cx.ath.jbzdak.zarlok.entities.ConfigEntry;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.MemManager;

public class TescConfigEntry {

	private static ZarlockDBManager  manager;

	private EntityManager em;

	@BeforeClass
	public static void setUp() throws Exception{
		manager = new MemManager();
		new DBSetup().setupDB(manager);
	}

	@AfterClass
	public static void tearDown() throws Exception{
		new DBClose().stopDatabase(manager);
	}

	@Before
	public void a(){
		em = manager.createEntityManager();
	}

	@Test
	public void testPersist(){
		ConfigEntry configEntry = new ConfigEntry();
		configEntry.setName("aaaaXXXXaaaa");
		em.getTransaction().begin();
		em.persist(configEntry);
		em.getTransaction().commit();
		Assert.assertNotNull(configEntry.getId());
	}


	@Test(expected=RollbackException.class)
	public void testValidator(){
		ConfigEntry configEntry = new ConfigEntry();
		configEntry.setName("aaaaXXXXaaaa");
		em.getTransaction().begin();
		em.persist(configEntry);
		em.getTransaction().commit();
		em.getTransaction().begin();
		configEntry.setValue("XXXX");
		em.merge(configEntry);
		em.getTransaction().commit();
	}


	@Test(expected=UnsupportedOperationException.class)
	public void testValidator2(){
		ConfigEntry configEntry = new ConfigEntry();
		configEntry.setName("aaaaXXXXaaaa");
		em.getTransaction().begin();
		em.persist(configEntry);
		em.getTransaction().commit();
		em.getTransaction().begin();
		em.remove(configEntry);
		em.merge(configEntry);
		em.getTransaction().commit();
	}


}
