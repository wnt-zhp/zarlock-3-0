package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.form.WrappedPanel;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.PartiaAdaptor;

import javax.persistence.EntityManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PartiaAddPanel extends PartiaAddPanelBasic {

	private static final long serialVersionUID = 1L;

	protected WrappedPanel<AutocompleteComboBox> produktSelectBox;

	public PartiaAddPanel(final DBManager manager) {
		super(manager);
		AutocompleteComboBox combobox = new AutocompleteComboBox(new PartiaAdaptor(manager));
		produktSelectBox = new WrappedPanel<AutocompleteComboBox>(combobox,"Produkt");
		add(produktSelectBox, "span 2");

		super.initGUI();
		combobox.addPropertyChangeListener("selectedValue", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getNewValue()!=null){
					ProductSearchCache psc = (ProductSearchCache) evt.getNewValue();
					EntityManager em = manager.createEntityManager();
					try{
						Produkt p = em.find(Produkt.class, psc.getProductId());
						setPartia(new Partia(p, psc));
						form.startEditing();
					}finally{
						em.close();
					}
				}
			}
		});
	}

	@Override
	protected void initGUI() { }

   public void reset(){
      produktSelectBox.getRenderer().setSelectedItem(null);
   }

}
