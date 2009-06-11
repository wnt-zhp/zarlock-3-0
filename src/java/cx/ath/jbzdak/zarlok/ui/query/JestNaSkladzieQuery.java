package cx.ath.jbzdak.zarlok.ui.query;

import javax.swing.RowFilter;

import java.math.BigDecimal;

public class JestNaSkladzieQuery<M, I> extends RowFilter<M, I> {

	private final int terazWMagColumn;

	boolean ignore;

	public JestNaSkladzieQuery(int terazWMagColumn) {
		super();
		this.terazWMagColumn = terazWMagColumn;
	}

	@Override
	public boolean include(
			javax.swing.RowFilter.Entry<? extends M, ? extends I> entry) {
		BigDecimal value = (BigDecimal) entry.getValue(terazWMagColumn);


		return ignore || value == null || BigDecimal.ZERO.compareTo(value) < 0;
	}

	public boolean isDisabled() {
		return ignore;
	}

	public void setDisabled(boolean ignore) {
		this.ignore = ignore;
	}

}
