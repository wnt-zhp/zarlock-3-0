package cx.ath.jbzdak.zarlok.ui.wyprowadzenie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.DAOFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.NotEmptyFormatter;
import cx.ath.jbzdak.zarlok.db.dao.PartiaDAO;
import cx.ath.jbzdak.zarlok.db.dao.WyprowadzenieDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.DataWyprowadzeniaFormatter;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.WyprowadzenieIloscJednostekFormatter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class WyprowadzenieAddPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private final PartiaDAO partiaDAO;

	private final WyprowadzenieDAO wyprowadzenieDAO;

	private final DAOForm<Wyprowadzenie, DAOFormElement> form;

	private final FormPanel<JTextField> nazwaPartii;

	private final FormPanel<MyFormattedTextField> iloscWydawana;

	private final WyprowadzenieIloscJednostekFormatter jednostekFormatter;

	private final FormPanel<MyFormattedTextField> dataWyprowadzenia;

	private final DataWyprowadzeniaFormatter dataWyprowadzeniaFormatter;

	private final FormPanel<MyFormattedTextField> tytulem;


	public WyprowadzenieAddPanel(DBManager manager) {
		super(new MigLayout("wrap 2, fillx", "[fill|fill]"));
		if(manager==null){
			throw new IllegalArgumentException();
		}
		this.wyprowadzenieDAO = new WyprowadzenieDAO(manager);
		this.partiaDAO = new PartiaDAO(manager);
		wyprowadzenieDAO.setAutoCreateEntity(true);
		FormFactory<Wyprowadzenie> factory = new FormFactory<Wyprowadzenie>();
		nazwaPartii = factory.decotrateJTextField("Nazwa partii:", "partia.basicData");
		jednostekFormatter = new WyprowadzenieIloscJednostekFormatter();
		iloscWydawana = factory.decorateFormattedTextField("Wydawana ilość", "iloscJednostek", jednostekFormatter);
		dataWyprowadzeniaFormatter = new DataWyprowadzeniaFormatter();
		dataWyprowadzenia = factory.decorateFormattedTextField("Data wyprowadzenia", "dataWyprowadzenia", dataWyprowadzeniaFormatter);
		tytulem = factory.decorateFormattedTextField("Tytułem:", "tytulem", new NotEmptyFormatter());
		form = factory.getCreatedForm();
		form.setDao(wyprowadzenieDAO);
		add(nazwaPartii, "span 2");
		add(iloscWydawana);
		add(dataWyprowadzenia);
		add(tytulem, "span 2");
	}


	public Wyprowadzenie getEntity() {
		return form.getEntity();
	}


	public void setEntity(Wyprowadzenie entity) {
		form.setEntity(entity);
		wyprowadzenieDAO.setEntity(entity);
	}


	public void setPartia(Partia entity) {
		partiaDAO.setEntity(entity);
		wyprowadzenieDAO.setEntity(new Wyprowadzenie(entity));
		jednostekFormatter.setPartia(entity);
		dataWyprowadzeniaFormatter.setP(entity);
	}


	DAOForm<Wyprowadzenie, DAOFormElement> getForm() {
		return form;
	}


	public void startEditing() {
		form.startEditing();
	}


	public void startViewing() {
		form.startViewing();
	}


	public void stopEditing() {
		form.rollback();
	}


	public void commit() {
		form.commit();
		partiaDAO.beginTransaction();
		try{
			partiaDAO.getEntity().getWyprowadzenia().add(wyprowadzenieDAO.getEntity());
			partiaDAO.getEntity().recalculateIloscTeraz();
			partiaDAO.getEntityManager().merge(wyprowadzenieDAO.getEntity());
			partiaDAO.update();
			partiaDAO.closeTransaction();
		}catch (Exception e) {
			e.printStackTrace();
			partiaDAO.rollback();
		}

	}


}
