package cx.ath.jbzdak.zarlock.ui.day.peopleNo;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import cx.ath.jbzdak.jpaGui.ui.form.*;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.IntegerFormatter;
import cx.ath.jbzdak.jpaGui.SimpleBeanHolder;
import cx.ath.jbzdak.zarlok.entities.PeopleNo;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 16, 2010
 */
public class PeopleNoPanel extends JPanel{

   private FormPanel uczestnicyPanel;
   private FormPanel kadraPanel;
   private FormPanel inniPanel;
   private SimpleBeanForm<PeopleNo> form;
   private SimpleBeanHolder<PeopleNo> beanHolder;

   public PeopleNoPanel() {
      super(new MigLayout("wrap 1, fillx, filly", "[grow, fill]"));
      FormFactory2<PeopleNo, SimpleBeanForm<PeopleNo>> formFactory = FormFactory2.createFactory(new SimpleBeanForm<PeopleNo>());
      uczestnicyPanel = formFactory.decorateFormattedTextField("peopleNo.uczestnicyNo", "uczestnicyNo", new IntegerFormatter());
      kadraPanel = formFactory.decorateFormattedTextField("peopleNo.kadraNo", "kadraNo", new IntegerFormatter());
      inniPanel = formFactory.decorateFormattedTextField("peopleNo.inniNo", "inniNo", new IntegerFormatter());
      form = formFactory.getCreatedForm();
      beanHolder = new SimpleBeanHolder<PeopleNo>();
      form.setBeanHolder(beanHolder);
      initLayout();
   }

   private void initLayout(){
      add(uczestnicyPanel);
      add(kadraPanel);
      add(inniPanel);
   }

   public PeopleNo getBean() {
      return beanHolder.getBean();
   }

   public void setBean(PeopleNo bean) {
      beanHolder.setBean(bean);
   }

   public SimpleBeanForm<PeopleNo> getForm() {
      return form;
   }

}
