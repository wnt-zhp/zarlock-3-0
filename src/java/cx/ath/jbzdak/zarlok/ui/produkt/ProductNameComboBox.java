package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import javax.persistence.EntityManager;

public class ProductNameComboBox extends AutocompleteComboBox {

	private static final long serialVersionUID = 4175012827461599756L;

	public ProductNameComboBox() {
		super(new ProductAutocompleteAdaptor());
	}

	public ProductNameComboBox(EntityManager em) {
		super(new ProductAutocompleteAdaptor(em));
	}
}
