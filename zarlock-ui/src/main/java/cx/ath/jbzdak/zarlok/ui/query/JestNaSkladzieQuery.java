package cx.ath.jbzdak.zarlok.ui.query;

import javax.swing.*;

public class JestNaSkladzieQuery<M, I> extends RowFilter<M, I> {

	private final int terazWMagColumn;

	boolean ignore;

   private final static double MIN = 0.01;

	public JestNaSkladzieQuery(int terazWMagColumn) {
		super();
		this.terazWMagColumn = terazWMagColumn;
	}

	@Override
	public boolean include(
			javax.swing.RowFilter.Entry<? extends M, ? extends I> entry) {
		Number value = (Number) entry.getValue(terazWMagColumn);
		return ignore || value == null || value.doubleValue() > MIN;
	}

	public boolean isDisabled() {
		return ignore;
	}

	public void setDisabled(boolean ignore) {
		this.ignore = ignore;
	}

}
