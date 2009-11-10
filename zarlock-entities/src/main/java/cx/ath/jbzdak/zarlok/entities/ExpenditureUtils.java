package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;

public class ExpenditureUtils {

	public static boolean compareContents(Expenditure w, Expenditure w2){
			if(!Utils.equals(w.getId(), w2.getId()))
				return false;
			if(w.getBatch()==null ^ w2.getBatch()==null){
				return false;
			}else if(w.getBatch()!=null && !Utils.equals(w.getBatch().getId(), w2.getBatch().getId()))
				return false;
			if(!Utils.equals(w.getQuantity(), w2.getQuantity()))
				return false;
			return true;
	}


	private static final PatternBeanFormatter beanFormatter = new PatternBeanFormatter("{posilek.dzien.data} na posiłek {posilek.nazwa} ");

	public static String getTytulemFromDanie(Course d){
		return beanFormatter.format(d);
	}

}
