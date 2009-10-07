package cx.ath.jbzdak.zarlok.ui.query;

import edu.umd.cs.findbugs.annotations.Nullable;

import javax.swing.*;
import java.util.Date;

public class WMagazynieQuery<M, I> extends RowFilter<M, I> {

	final int dataWaznosciIndex, dataKsiegowaniaIndex;

	@Nullable
	Date data;

	public WMagazynieQuery(int dataWaznosciIndex, int dataKsiegowaniaIndex) {
		super();
		this.dataWaznosciIndex = dataWaznosciIndex;
		this.dataKsiegowaniaIndex = dataKsiegowaniaIndex;
	}

	@Override
	public boolean include(
			javax.swing.RowFilter.Entry<? extends M, ? extends I> entry) {
		return data == null || data.before(((Date) entry.getValue(dataWaznosciIndex)))
				&& data.after(((Date) entry.getValue(dataKsiegowaniaIndex)));
	}

	@Nullable
	public Date getData() {
		return data;
	}

	public void setData(@Nullable Date data) {
		this.data = data;
	}

}
