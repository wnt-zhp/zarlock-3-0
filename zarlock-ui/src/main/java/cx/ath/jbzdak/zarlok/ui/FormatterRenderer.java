package cx.ath.jbzdak.zarlok.ui;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class FormatterRenderer extends DefaultTableCellRenderer{

	public FormatterRenderer(MyFormatter myFormatter) {
		super();
		this.myFormatter = myFormatter;
	}

	private static final long serialVersionUID = 1L;

	private static final Logger logger = makeLogger();

	private final MyFormatter myFormatter;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(value==null){
			logger.debug("value is null "  + myFormatter.getClass());
		}
		try {
			return super.getTableCellRendererComponent(table, myFormatter.formatValue(value), isSelected, hasFocus,
					row, column);
		} catch (FormattingException e) {
			logger.warn("",e);
			return super.getTableCellRendererComponent(table, "", isSelected, hasFocus,
					row, column);
		}
	}

}
