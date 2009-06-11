package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import static cx.ath.jbzdak.jpaGui.Utils.valueOf;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ProductNameFormatter implements MyFormatter {

	private final EntityManager entityManager;

	private Produkt prod;

	public ProductNameFormatter(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public String formatValue(Object value) throws FormattingException {
		return valueOf(value);
	}

	@Override
	public Object parseValue(String text) throws Exception {
		if(prod.getId()!=null){
			if(!prod.getNazwa().equals(text)){
				throw new FormattingException("Nie można zmienić nazwy istniejącego produktu");
			}
		}else{
			Query q = entityManager.createNamedQuery("countProduktNazwa").setParameter("nazwa", text);
			if(((Number)q.getSingleResult()).intValue()!=0){
				throw new FormattingException("Dany produkt już istnieje");
			}
		}
		return text;
	}

	public Produkt getProd() {
		return prod;
	}

	public void setProd(Produkt prod) {
		this.prod = prod;
	}
}
