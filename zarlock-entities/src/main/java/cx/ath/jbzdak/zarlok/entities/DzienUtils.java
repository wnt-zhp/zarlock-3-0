package cx.ath.jbzdak.zarlok.entities;

import java.util.ArrayList;
import java.util.List;


public class DzienUtils {

	public static List<Meal> getDefaultPosilki(Day d){
		List<Meal> posilki = new ArrayList<Meal>();
		posilki.add(new Meal("Śniadanie", d));
		posilki.add(new Meal("Obiad", d));
		posilki.add(new Meal("Podwieczorek", d));
		posilki.add(new Meal("Kolacja", d));
		return posilki;
	}
}
