package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.common.famfamicons.IconManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DniTreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	protected Object value;


	@Override
	public Icon getClosedIcon() {
      if(value==null) return super.getClosedIcon();
      String iconName = value.getClass().getSimpleName().toLowerCase() + "_closed";
		return IconManager.getIconSafe(iconName);
	}

	@Override
	public Icon getLeafIcon() {
     if(value==null) return super.getLeafIcon();
      String iconName = value.getClass().getSimpleName().toLowerCase() + "_leaf";
		return IconManager.getIconSafe(iconName);
	}

	@Override
	public Icon getOpenIcon() {
      if(value==null) return super.getOpenIcon();
		String iconName = value.getClass().getSimpleName().toLowerCase() + "_open";
		return IconManager.getIconSafe(iconName);
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
