package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.ProductAdaptor;

public class ProductSearchBox extends AutocompleteComboBox {

	private static final long serialVersionUID = 1L;

	public ProductSearchBox(DBManager manager) {
		super(new ProductAdaptor(manager));
		init();
	}

	public ProductSearchBox() {
		super();
		init();
	}
	
	private void init(){
		setStrict(true);
		setIgnoreError(true);
	}

	public void setManager(DBManager manager){
		setAdaptor(new ProductAdaptor(manager));
	}
}
