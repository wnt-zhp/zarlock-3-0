package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.JednostkaAdaptor;

public class ProduktJednostkaComboBox extends AutocompleteComboBox{

	private static final long serialVersionUID = 1L;

	public ProduktJednostkaComboBox(DBManager manager) {
		super(new JednostkaAdaptor(manager));

	}

}
