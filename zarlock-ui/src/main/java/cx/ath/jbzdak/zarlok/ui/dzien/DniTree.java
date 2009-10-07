package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.Posilek;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;


public class DniTree extends JTree {

	private static final long serialVersionUID = 1L;

	private final PatternBeanFormatter dzienFormatter
		= new PatternBeanFormatter("<html><strong>{data}</strong>({iloscOsob.suma} osób na terenie)</html>");

	private final PatternBeanFormatter posilekFormatter
		= new PatternBeanFormatter("<html><strong>{nazwa}</strong> (koszt: {#0}{(costStrict)?\"\":\"<em>\"}{koszt}{#0}{(costStrict)?\"\":\"</em>\"}zł)</html>");

   private final PatternBeanFormatter danieFormatter
      = new PatternBeanFormatter("<html><strong>{nazwa}</strong> (koszt: {#0}{(costStrict)?\"\":\"<em>\"}{koszt}{#0}{(costStrict)?\"\":\"</em>\"}zł)</html>");

	@Override
	public String convertValueToText(Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		value = ((DefaultMutableTreeNode)value).getUserObject();
      if(value==null){
         return "";
      }
		if (value instanceof Dzien) {
			Dzien d = (Dzien) value;
			return dzienFormatter.format(d);
		}

		if (value instanceof Posilek) {
			Posilek p = (Posilek) value;
			return posilekFormatter.format(p);
		}


      if (value instanceof Danie) {
            Danie d = (Danie) value;
            return danieFormatter.format(d);
         }

		return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
	}






}
