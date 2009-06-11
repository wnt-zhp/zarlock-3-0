package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteAdaptor;

/**
 * Nic nie robiący adaptor. 
 * @author jb
 *
 */
public class NoopAdaptor<T> extends AutoCompleteAdaptor<T>{

	private static final long serialVersionUID = 1L;

	@Override
	protected void onFilterChange() {
		
	}

	@Override
	public T getValueHolderFromFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
