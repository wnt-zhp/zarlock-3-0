package cx.ath.jbzdak.zarlok.ui.query;

import cx.ath.jbzdak.jpaGui.ui.query.FulltextFilter;

import javax.swing.*;

public class StanMagazynuFilter<M, I> extends RowFilter<M, I>{

	private final FulltextFilter fulltextFilter;

	private final JestNaSkladzieQuery<M, I> jestNaSkladzieQuery;

    public StanMagazynuFilter() {
		super();
		this.fulltextFilter = new FulltextFilter(0,1,2);
		this.jestNaSkladzieQuery = new JestNaSkladzieQuery<M,I>(4);
	}

	@Override
	public boolean include(
			javax.swing.RowFilter.Entry<? extends M, ? extends I> entry) {
		return fulltextFilter.include(entry) && jestNaSkladzieQuery.include(entry);
	}

	public boolean isFuzzy() {
		return fulltextFilter.isFuzzy();
	}

	public void setFuzzy(boolean fuzzy) {
		fulltextFilter.setFuzzy(fuzzy);
	}

	public void setQuery(String query) {
		fulltextFilter.setQuery(query);
	}
	public boolean isJestNaSkladzie() {
		return !jestNaSkladzieQuery.isDisabled();
	}

	public void setJestNaSkladzie(boolean ignore) {
		jestNaSkladzieQuery.setDisabled(!ignore);
	}

	public FulltextFilter getFulltextQuery() {
		return fulltextFilter;
	}

	public JestNaSkladzieQuery<M, I> getJestNaSkladzieQuery() {
		return jestNaSkladzieQuery;
	}




}
