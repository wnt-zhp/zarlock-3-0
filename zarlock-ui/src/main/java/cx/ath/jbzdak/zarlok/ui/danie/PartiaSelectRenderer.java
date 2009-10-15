package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.zarlok.entities.Partia;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;

public class PartiaSelectRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
			Partia p = (Partia) value;
			if(p.getIloscTeraz().equals(BigDecimal.ZERO)){
				setEnabled(false);
			}
			Date today = new Date();
			if( p.getDataKsiegowania().after(today)){
				setForeground(Color.BLUE);
				if(cellHasFocus){
					setBackground(Color.MAGENTA);
				}
			}
			if(p.getDataWaznosci()!=null && p.getDataWaznosci().before(today)){
				setForeground(Color.RED);
			}
			setText(p.getSearchFormat());
			return this;
	}

}
