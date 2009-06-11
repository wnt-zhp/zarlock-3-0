package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;

public class WyprowadzenieUtils {

	public static final boolean compareContents(Wyprowadzenie w, Wyprowadzenie w2){
			if(!Utils.equals(w.getId(), w2.getId()))
				return false;
			if(w.getPartia()==null ^ w2.getPartia()==null){
				return false;
			}else if(w.getPartia()!=null && !Utils.equals(w.getPartia().getId(), w2.getPartia().getId()))
				return false;
			if(!Utils.equals(w.getIloscJednostek(), w2.getIloscJednostek()))
				return false;
			return true;
	}


	private final static PatternBeanFormatter beanFormatter = new PatternBeanFormatter("Wydano na danie {nazwa}, posiłek {posilek.nazwa}, dnia {posilek.dzien.data}");

	public static final String getTytulemFromDanie(Danie d){
		return beanFormatter.format(d);
	}

}
