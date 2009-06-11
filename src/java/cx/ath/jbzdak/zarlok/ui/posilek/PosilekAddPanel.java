package cx.ath.jbzdak.zarlok.ui.posilek;

import cx.ath.jbzdak.jpaGui.ui.form.*;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.NonNullFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.IloscOsob;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.ui.iloscOsob.IloscOsobPanel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-02
 */
public class PosilekAddPanel extends JPanel{

   FormPanel<MyFormattedTextField> nazwaPanel;

   IloscOsobPanel ilocOsobPanel;

   DAOForm<Posilek, ? extends DAOFormElement> form;

   public PosilekAddPanel(DBManager manager){
      super(new MigLayout("wrap 1", "[fill, grow]"));
      FormFactory formFactory = new FormFactory();
      nazwaPanel = formFactory.decorateFormattedTextField("Nazwa", "nazwa", new NonNullFormatter());
      ilocOsobPanel = new IloscOsobPanel();
      add(nazwaPanel);
      add(ilocOsobPanel);
      form = formFactory.getCreatedForm();
      form.setDao(manager.getDao(Posilek.class));
   }

   public Posilek getPosilek() {
      ilocOsobPanel.commit();
      Posilek p = form.getEntity();
      p.setIloscOsob(ilocOsobPanel.getEntity());
      form.commit();
      return p;
   }

   public void setPosilek(Posilek posilek) {
      form.setEntity(posilek);
      ilocOsobPanel.setEntity(posilek.getIloscOsob());
   }

}
