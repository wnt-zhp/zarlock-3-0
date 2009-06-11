package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.awt.Component;

public class DniTreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	protected Object value;


	@Override
	public Icon getClosedIcon() {
		return IconManager.getIconSafe("book");
	}

	@Override
	public Icon getLeafIcon() {
		if (value instanceof Dzien) {
			return getClosedIcon();
		}
		return IconManager.getIconSafe("basket");
	}

	@Override
	public Icon getOpenIcon() {
		return IconManager.getIconSafe("book_open");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		this.value = ((DefaultMutableTreeNode)value).getUserObject();
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
	}
}
