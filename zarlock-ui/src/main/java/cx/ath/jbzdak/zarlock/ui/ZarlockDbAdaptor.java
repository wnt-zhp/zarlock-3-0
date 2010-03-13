package cx.ath.jbzdak.zarlock.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;

import cx.ath.jbzdak.jpaGui.ui.autoComplete.SwingWorkerAdaptor;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 10, 2010
 */
public abstract class ZarlockDbAdaptor<V> extends SwingWorkerAdaptor<V>{

   private static final Logger LOGGER = LoggerFactory.getLogger(ZarlockDbAdaptor.class);

   final Component owner;

   public ZarlockDbAdaptor(Component owner) {
      this.owner = owner;
      if(owner == null){
         throw new IllegalArgumentException();
      }
   }


   /**
	 * Tutaj wykonujemy odpowiedż na ustawienie filtra. Filt jest dostępny
	 * poprzez {@link #getFilter()}
	 * @return Wyniki przeszukiwania.
	 */
	protected abstract Collection<V>  doInBackground(EntityManager manager);

   @Override
   protected Collection<V> doInBackground() {
      ZarlockModel model = ZarlockUtils.getZarlockModel(owner);
      if(model == null){
         LOGGER.info("ZarlockModel is null {}");
         return Collections.emptyList();
      }
      EntityManager manager = model.getDBManager().createProvider();
      try {
         return doInBackground(manager);
      } finally {
         manager.close();
      }
   }

   @Override
   protected void done() {
      setCurentFilteredResults(getUnsafe());
   }
}
