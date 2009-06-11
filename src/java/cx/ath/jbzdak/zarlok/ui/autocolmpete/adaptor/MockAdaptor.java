package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteAdaptor;
import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Testowya adapter
 * @author jb
 *
 */
public class MockAdaptor extends AutoCompleteAdaptor<AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;

	public static final List<AutoCompleteValueHolder> unfiltered =
		Arrays.asList(
				new AutoCompleteValueHolder("AAA"),
				new AutoCompleteValueHolder("AAB"),
				new AutoCompleteValueHolder("ABA"),
				new AutoCompleteValueHolder("CBA"),
				new AutoCompleteValueHolder("CAB")
			);

	public MockAdaptor(){
		setCurentFilteredResults(createList(""));
	}

	private List<AutoCompleteValueHolder> createList(String filter){
		List<AutoCompleteValueHolder> filtered = new  ArrayList<AutoCompleteValueHolder>();
		for(AutoCompleteValueHolder s : unfiltered){
			if(s.getLabel().startsWith(filter)){
				filtered.add(s);
			}
		}
		return filtered;
	}

	@Override
	protected void onFilterChange() {
		setCurentFilteredResults(createList(getFilter()));

	}

	@Override
	public AutoCompleteValueHolder getValueHolderFromFilter() {
		// TODO Auto-generated method stub
		return null;
	}



}
