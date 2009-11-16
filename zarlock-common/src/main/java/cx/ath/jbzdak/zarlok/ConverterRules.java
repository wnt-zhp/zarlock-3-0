package cx.ath.jbzdak.zarlok;

import cx.ath.jbzdak.zarlok.entities.UnitConverter;

import java.util.Collection;
import java.util.List;

public interface ConverterRules {
	
	List<UnitConverter> pruneUnneeded(UnitConverter... converters);
	
	List<UnitConverter> findConsequences(UnitConverter... converters); 
	
	//TODO kiedyś
	//List<UnitConverter> findConsequencesOfRemoval(UnitConverter... converter);

   void performChanges(Collection<UnitConverter> converters);

}
