package cx.ath.jbzdak.zarlok.entities;

import java.util.ArrayList;
import java.util.List;


public class DzienUtils {

	public static List<Posilek> getDefaultPosilki(Dzien d){
		List<Posilek> posilki = new ArrayList<Posilek>();
		posilki.add(new Posilek("Śniadanie", d));
		posilki.add(new Posilek("Obiad", d));
		posilki.add(new Posilek("Podwieczorek", d));
		posilki.add(new Posilek("Kolacja", d));
		return posilki;
	}
}
